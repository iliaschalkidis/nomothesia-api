package com.di.nomothesia.dao;

import com.di.nomothesia.comparators.LegalDocumentSort;
import com.di.nomothesia.model.Chapter;
import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.Citation;
import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.model.Paragraph;
import com.di.nomothesia.model.Part;
import com.di.nomothesia.model.Passage;
import com.di.nomothesia.model.Signer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.OpenRDFException;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
//import eu.earthobservatory.org.StrabonEndpoint.client.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.Update;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
//import org.openrdf.query.resultio.stSPARQLQueryResultFormat;


public class LegalDocumentDAOImpl implements LegalDocumentDAO {

    @Override
    public LegalDocument getMetadataById(String decisionType, String year, String id) {
        
        LegalDocument legald = new LegalDocument();
        legald.setId(id);
        legald.setYear(year);
        legald.setDecisionType(decisionType);
        
        legald.setURI("http://legislation.di.uoa.gr/"+ decisionType + "/" + year + "/" + id);
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT DISTINCT ?title ?date ?gaztitle ?signername ?views ?place ?htmltitle\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " dc:created ?date.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:gazette ?gazette.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:views ?views.\n" +
                "?gazette dc:title ?gaztitle.\n" +
                //"?gazette leg:pdfFile ?pdfile.\n" +
                "OPTIONAL{<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " dc:title ?title.}\n" +
                "OPTIONAL{<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:html ?htmltitle.}\n" +
                "OPTIONAL{ <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +"> leg:place ?place.}"+
                "FILTER(langMatches(lang(?title), \"el\"))\n"+
                "}";
                  
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    int flag = 0;
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next(); 
                        Signer sign = new Signer();
                        legald.setURI("http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id);
                        String title_el = "";
                        if(bindingSet.hasBinding("title")){
                            title_el = bindingSet.getValue("title").toString().replace("@el", "");
                        }
                        else{
                            title_el = legald.getDecisionType() + "" + legald.getYear() + " " + legald.getId();
                        }
                        if(bindingSet.hasBinding("htmltitle")){
                            title_el = bindingSet.getValue("htmltitle").toString().split("\\^\\^",2)[0];
                        }
                        legald.setTitle(trimDoubleQuotes(title_el));
                        String date2 = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
                        legald.setPublicationDate(trimDoubleQuotes(date2));
                        String fek = bindingSet.getValue("gaztitle").toString().replace("^^","");
                        legald.setFEK(trimDoubleQuotes(fek));
                        //String fekfile = bindingSet.getValue("pdfile").toString().replace("^^","");
                        //legald.setFEKfile(trimDoubleQuotes(fekfile));
                       String views = bindingSet.getValue("views").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                        legald.setViews(trimDoubleQuotes(views));
                        
//                        if((flag ==0) &&(bindingSet.hasBinding("place"))){
//                            flag = 1;
//                            legald.setPlace(this.getKML(bindingSet.getValue("place").toString()));
//                        }
                    }
                    
                }
                catch (Exception ex) {
                    Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }                finally {
                    result.close();
                }
                
                //GET TAGS
                String queryString2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT ?tag\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:tag ?tag.\n" +
                "FILTER(langMatches(lang(?tag), \"el\"))\n"+    
                "}";
                  
                //System.out.println(queryString2);
                TupleQuery tupleQuery2 = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString2);
                result = tupleQuery2.evaluate();

                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next(); 
                        String tag_el = bindingSet.getValue("tag").toString().replace("@el", "");
                        legald.getTags().add(trimDoubleQuotes(tag_el));
                    
                    }
                    
                }
                finally {
                    result.close();
                }
                
                //GET SIGNERS
                 String queryString3 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT DISTINCT ?signername ?html\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:signer ?signer.\n" +
                "?signer foaf:name ?signername.\n" +
                //"?signer foaf:title ?signertitle.\n" +
                "OPTIONAL{?signer leg:html ?html.}\n"+
                "FILTER(langMatches(lang(?signername), \"el\"))\n"+
                "}";
                  
                //System.out.println(queryString2);
                TupleQuery tupleQuery3 = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString3);
                result = tupleQuery3.evaluate();

                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next(); 
                        Signer sign = new Signer();
                        String name_el = bindingSet.getValue("signername").toString().replace("@el", "");
                        if(bindingSet.getValue("html")!=null){
                            name_el = bindingSet.getValue("html").toString().split("\\^\\^",2)[0];
                        }
                        sign.setFullName(trimDoubleQuotes(name_el));
                        //String signer_el = bindingSet.getValue("signertitle").toString().replace("@el", "");
                        //sign.setTitle(trimDoubleQuotes(signer_el));
                        legald.getSigners().add(sign);
                    
                    }
                    
                }
                finally {
                    result.close();
                }
                 
            
            try{
            int views = Integer.parseInt(legald.getViews())+1;
            //UPDATE VIEWS
            String queryString4 = "DELETE {\n" +
            "   <"+legald.getURI()+"> <http://legislation.di.uoa.gr/ontology/views> ?views\n" +
            "}\n" +
            "INSERT {\n" +
            "   <"+legald.getURI()+"> <http://legislation.di.uoa.gr/ontology/views> \""+views+"\"^^<http://www.w3.org/2001/XMLSchema#integer>\n" +
            "}\n";

            Update update = con.prepareUpdate(QueryLanguage.SPARQL, queryString4);
            update.execute();
            }
            catch(Exception ex){
                
            }
            finally {
                con.close();
            }
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        
        
        return legald;

    }
    
    @Override
    public LegalDocument getCitationsById(String decisionType, String year, String id, int req, LegalDocument legald) {
        
        //LegalDocument legald = this.getMetadataById(decisionType, year, id);
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
           
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT  DISTINCT ?citation ?cittext ?cituri ?cithtml\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +"> metalex:part ?citation.\n" +
                "?citation a metalex:BibliographicCitation.\n" +
                "?citation leg:context ?cittext.\n"+
                "OPTIONAL {?citation metalex:cites ?cituri.}.\n" +
                "OPTIONAL {?citation leg:html ?cithtml.}\n";
                
                if (req == 1) {
                    queryString += "}";
                }
                else if (req == 2) {
                    queryString += "FILTER(langMatches(lang(?cittext), \"el\"))\n" +
                    "}";
                }
                
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                      
                    int cid = 1;
                    String old2 = "old2";
                    String old = "old";
                    String current = "current";
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        current = trimDoubleQuotes(bindingSet.getValue("citation").toString());
                        
                        if (current.equals(old)) {
                                
                            if (bindingSet.getValue("cituri") != null) {
                                if (old2.equals(bindingSet.getValue("cittext").toString()) && !bindingSet.getValue("cittext").toString().contains("@html")) {
                                    Citation cit = new Citation();
                                    cit = legald.getCitations().get(cid-2);
                                    cit.gettargetURIs().add(trimDoubleQuotes(bindingSet.getValue("cituri").toString()));
                                }
                                
                                old = current;
                                old2 = bindingSet.getValue("cittext").toString();
                            
                            }
                                
                        }
                        else {
                                
                            Citation cit = new Citation();
                            cit.setURI(trimDoubleQuotes(bindingSet.getValue("citation").toString()));

                            if (bindingSet.getValue("cituri") != null) {
                                cit.gettargetURIs().add(trimDoubleQuotes(bindingSet.getValue("cituri").toString()));
                            }
                                
                            if (bindingSet.getValue("cittext").toString().contains("@html")) {
                                String text = bindingSet.getValue("cittext").toString().replace("@html", "");
                                cit.setDescription(trimDoubleQuotes(text));
                            }
                            else if (bindingSet.getValue("cittext").toString().contains("@el")) {
                                String text = bindingSet.getValue("cittext").toString().replace("@el", "");
                                cit.setDescription(trimDoubleQuotes(text));
                            }
                            
                            if(bindingSet.getValue("cithtml")!=null){
                                String text = bindingSet.getValue("cithtml").toString().split("\\^\\^",2)[0];
                                cit.setDescription(trimDoubleQuotes(text));
                            }
                            
                            cit.setId(cid);
                            cid++;
                            legald.getCitations().add(cit);
                            old = current;
                            old2 = bindingSet.getValue("cittext").toString();

                        }
                        
                    }
                    
                }
                finally {
                    result.close();
                }    
            }
            finally {
                con.close();
            }
        }
        catch (OpenRDFException e) {
            // handle exception
        }
         
        /*System.out.println("========================================================================================="); 
        for(int i=0; i < legald.getCitations().size(); i++){
            System.out.println(legald.getCitations().get(i).getDescription() + "\n" + legald.getCitations().get(i).getURI() + "\n");
            for(int j=0; j < legald.getCitations().get(i).gettargetURIs().size(); j++){
                System.out.println(legald.getCitations().get(i).gettargetURIs().get(j) + "\n");
            }
        }
        System.out.println("=========================================================================================");*/
        
        return legald;

    }
    
    @Override
    public LegalDocument getById(String decisionType, String year, String id, int req, LegalDocument legald) {
        
        //LegalDocument legald = this.getCitationsById(decisionType, year, id, req);
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
         
        try {
           
            RepositoryConnection con = repo.getConnection();
           
            try {
                
                    String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT DISTINCT ?part ?text ?html ?type ?title ?filename\n" +
                    "WHERE{\n" +
                    "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:part+  ?part.\n" +
                    "?part rdf:type ?type.\n" +
                    "OPTIONAL{ ?part leg:text ?text.}.\n" +
                    "OPTIONAL{ ?part leg:html ?html.}.\n" +
                    "OPTIONAL{ ?part dc:title ?title.}.\n"+
                    "OPTIONAL{ ?part leg:imageName ?filename.}."+
                    "}\n"+
                    "ORDER BY ?part";
                  
                    //System.out.println(queryString);
                    TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                    result = tupleQuery.evaluate();

                    try {
                    
                        // iterate the result set
                        int part_count = -1;
                        int chap_count = -1;
                        int art_count = -1;
                        int art_count2= 0;
                        int count2 = -1;
                        int count3 = -1;
                        int count4 = -1;
                        int mod = 0;
                        int mod_count3 = -1;
                        int mod_count4 = -1;
                        String old = "old";
                        Paragraph paragraph = null;
                        Article article = null;
                    
                        while (result.hasNext()) {
                            
                            BindingSet bindingSet = result.next(); 
                            
                            if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Chapter")) {
                                
                                Chapter chapter = new Chapter();
                                chapter.setURI(bindingSet.getValue("part").toString());
                                if(mod==1&&chapter.getURI().contains("modification")){
                                    chapter.setId(Integer.parseInt(chapter.getURI().split("chapter\\/")[2].replaceAll("[Î“Â?-Î“Â™]+","")));
                                }
                                else{
                                   chapter.setId(Integer.parseInt(chapter.getURI().split("chapter\\/")[1].replaceAll("[Î“Â?-Î“Â™]+","")));
                                }
                                if (bindingSet.getValue("title")!=null) {
                                    String title = bindingSet.getValue("title").toString().replace("@el", "");
                                    title = title.replace("^^", "");
                                    chapter.setTitle(trimDoubleQuotes(title));
                                }
                                
                                //System.out.println(article.getURI());
                                //System.out.println("NEW CHAPTER"+chap_count);
                                if(!legald.getParts().isEmpty()){
                                    legald.getParts().get(part_count).getChapters().add(chapter);
                                }
                                else{
                                    legald.getChapters().add(chapter);
                                }
                                chap_count ++;
                                art_count =-1;
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                mod = 0;
                                int mod_count = 3;
                               
                            
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Part")){
                                Part part = new Part();
                                part.setURI(bindingSet.getValue("part").toString());
                                if(mod==1&&part.getURI().contains("modification")){
                                    part.setId(Integer.parseInt(part.getURI().split("part\\/")[2].replaceAll("[Î“Â?-Î“Â™]+","")));
                                }
                                else{
                                    part.setId(Integer.parseInt(part.getURI().split("part\\/")[1].replaceAll("[Î“Â?-Î“Â™]+","")));
                                }
                                if (bindingSet.getValue("title")!=null) {
                                    String title = bindingSet.getValue("title").toString().replace("@el", "");
                                    title = title.replace("^^", "");
                                    part.setTitle(trimDoubleQuotes(title));
                                }
                                
                                //System.out.println(article.getURI());
                                //System.out.println("NEW CHAPTER"+chap_count);
                                legald.getParts().add(part);
                                part_count ++;
                                chap_count = -1;
                                art_count =-1;
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                mod = 0;
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Article")) {
                                
                                article = new Article();
                                article.setURI(bindingSet.getValue("part").toString());
                                if(mod==1&&article.getURI().contains("modification")){
                                    article.setId(article.getURI().split("article\\/")[2]);
                                }
                                else{
                                    article.setId(article.getURI().split("article\\/")[1]);
                                }
                                if (bindingSet.getValue("title")!=null) {
                                    String title = bindingSet.getValue("title").toString().replace("@el", "");
                                    title = title.replace("^^", "");
                                    article.setTitle(trimDoubleQuotes(title));
                                }
                                
                                //System.out.println(article.getURI());
                                //System.out.println("NEW ARTICLE");
                                if((mod==0)||(mod==2)||(mod==3)){
                                    //System.out.println("NEW ARTICLE"+art_count);
                                    if(!legald.getParts().isEmpty()){
                                        if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                            legald.getParts().get(part_count).getArticles().add(article);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().add(article);
                                        }
                                    }
                                    else{
                                        if(legald.getChapters().isEmpty()){
                                            legald.getArticles().add(article);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().add(article);
                                        }
                                    }
                                    
                                    art_count ++;
                                    art_count2 ++;
                                    count2 = -1;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                }
                                else if(article.getURI().contains("modification")){
                                    //System.out.println("MODIFICATION ARTICLE");
                                    mod = 3;
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Article");
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Article");
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                        else{
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Article");
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                               // System.out.println("=======================================================================================");
                                                //System.out.println(article.getURI());
                                                //System.out.println("ERT:  chapter/"+chap_count+"/article/"+art_count+"/paragraph/"+count2+"/passage/"+count4);
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Article");
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                    }
                                    else{
                                       if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Article");
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Article");
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                        else{
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Article");
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                //System.out.println("=======================================================================================");
                                               // System.out.println(article.getURI());
                                                //System.out.println("ERT:  chapter/"+chap_count+"/article/"+art_count+"/paragraph/"+count2+"/passage/"+count4);
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Article");
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        } 
                                    }
                                    mod_count3 = -1;
                                    mod_count4 = -1;
                                }
                                else{
                                    //System.out.println("NEW ARTICLE"+art_count);
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                            legald.getArticles().add(article);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().add(article);
                                        }
                                    }
                                    else{
                                        if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                            legald.getParts().get(part_count).getArticles().add(article);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().add(article);
                                        }
                                    }
                                    art_count ++;
                                    art_count2 ++;
                                    count2 = -1;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                    
                                }
                               
                                
                            
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Image")) {
                                String number = bindingSet.getValue("part").toString().split("image\\/")[1];
                                if(legald.getParts().isEmpty()){
                                    if(legald.getChapters().isEmpty()){
                                        legald.getArticles().get(art_count).getParagraphs().get(count2).getImages().add(trimDoubleQuotes(bindingSet.getValue("filename").toString().split("\"")[1]));
                                    }
                                    else{
                                        legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getImages().add(trimDoubleQuotes(bindingSet.getValue("filename").toString().split("\"")[1]));
 
                                    }
                                }
                                else{
                                    if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                        legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getImages().add(trimDoubleQuotes(bindingSet.getValue("filename").toString().split("\"")[1]));
                                    }
                                    else{
                                        legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getImages().add(trimDoubleQuotes(bindingSet.getValue("filename").toString().split("\"")[1]));
 
                                    }
                                }
                                legald.getImages().add(trimDoubleQuotes(bindingSet.getValue("filename").toString().split("\"")[1]));
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")) {
                                
                                paragraph = new Paragraph();
                                
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                if(mod>=1&&paragraph.getURI().contains("modification")){
                                    paragraph.setId(paragraph.getURI().split("paragraph\\/")[2]);
                                }
                                else{
                                    paragraph.setId(paragraph.getURI().split("paragraph\\/")[1]);
                                }
                                
                                if ((mod==0)||(mod==2)||((mod==1)&&(!paragraph.getURI().contains("modification")))) {
                                    //System.out.println("NEW PARAGRAPH"+count2);
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                             legald.getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                    }
                                    else{
                                       if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                             legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }  
                                    }
                                    //legald.getArticles().get(art_count).getParagraphs().add(paragraph);
                                    count2++;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                }
                                else if((mod==1)&&(paragraph.getURI().contains("modification"))){
                                    //System.out.println("MODIFICATION PARAGRAPH");
                                    mod = 2;
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                            if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Paragraph");
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                            }
                                            else{
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Paragraph");
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                            }
                                        }
                                        else{
                                            if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Paragraph");
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Paragraph");
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                            }
                                        }
                                    }
                                    else{
                                        if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                            if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Paragraph");
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Paragraph");
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                            }
                                        }
                                        else{
                                            if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Paragraph");
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Paragraph");
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                            }
                                        }
                                    }
                                    mod_count3 = -1;
                                    mod_count4 = -1;
                                }
                                else if ((mod==3)&&(paragraph.getURI().contains("modification"))){
                                    //System.out.println("MODIFICATION ARTICLE PARAGRAPH"+count2);
                                    article.getParagraphs().add(paragraph);
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                        else{
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                    }
                                    else{
                                        if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                        else{
                                            if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                            }
                                        }
                                    }
                                    mod_count3 = -1;
                                    mod_count4 = -1;
                                }
                                else if ((mod==3)&&(!paragraph.getURI().contains("modification"))){
                                    //System.out.println("NEW PARAGRAPH"+count2);
                                    if(legald.getParts().isEmpty()){
                                        if(legald.getChapters().isEmpty()){
                                             legald.getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                    }
                                    else{
                                        if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                             legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().add(paragraph);
                                        }
                                    }
                                    count2++;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")) {
                                
                                if (!old.equals(bindingSet.getValue("part").toString())) {
                                    
                                    Passage passage = new Passage();
                                    
                                    passage.setURI(bindingSet.getValue("part").toString());
                                    
                                    if (req==1&&bindingSet.getValue("html")!=null) {
                                        String text = bindingSet.getValue("html").toString().split("\\^\\^",2)[0];
                                        passage.setText(trimDoubleQuotes(text));
                                    }
                                    else{
                                        String text = bindingSet.getValue("text").toString().replace("@el", "");
                                        passage.setText(trimDoubleQuotes(text));
                                    }
                                    
                                    //System.out.println(passage.getURI());

                                    if ((mod==0)||(!passage.getURI().contains("modification"))) {
                                        passage.setId(count3+2);
                                        //System.out.println("NEW PASSAGE"+count3);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().add(passage);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().add(passage);
                                            }
                                        }
                                        else{
                                           if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().add(passage);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().add(passage);
                                            } 
                                        }
                                        count3 ++;
                                    }
                                    else if (mod==1) {
                                        passage.setId(mod_count3+2);
                                        //System.out.println("MODIFICATION PASSAGE");
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(passage.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Passage");
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(passage);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Passage");
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(passage);
                                                }
                                            }
                                            else{
                                                if(passage.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Passage");
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(passage);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Passage");
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(passage);
                                                }
                                            }
                                        }
                                        else{
                                             if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(passage.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Passage");
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(passage);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Passage");
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(passage);
                                                }
                                            }
                                            else{
                                                if(passage.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Passage");
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(passage);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Passage");
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(passage);
                                                }
                                            }
                                        }
                                        mod =0;
                                        mod_count3 ++;
                                    }
                                    else if (mod==2){
                                        passage.setId(mod_count3+2);
                                        //System.out.println("PARAGRAPH MODIFICATION PASSAGE");
                                        paragraph.getPassages().add(passage);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                            else{
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                            else{
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                        }
                                        mod_count3 ++;
                                    }
                                    else if ((mod==3)&&(passage.getURI().contains("modification"))){
                                        passage.setId(mod_count3+2);
                                        //System.out.println("ARTICLE PARAGRAPH MODIFICATION PASSAGE");
                                        article.getParagraphs().get(article.getParagraphs().size()-1).getPassages().add(passage);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                            else{
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                            else{
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                        }
                                        mod_count3 ++;
                                    }
                                    else if ((mod==3)&&(!passage.getURI().contains("modification"))){
                                        passage.setId(count3+2);
                                        //System.out.println("NEW PASSAGE"+count3);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                legald.getArticles().get(art_count).getParagraphs().get(legald.getArticles().get(art_count).getParagraphs().size()-1).getPassages().add(passage);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get( legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().size()-1).getPassages().add(passage);
                                            }
                                        }
                                        else{
                                             if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(legald.getArticles().get(art_count).getParagraphs().size()-1).getPassages().add(passage);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get( legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().size()-1).getPassages().add(passage);
                                            }
                                        }
                                        mod=0;
                                        count3 ++;
                                    }

                                    
                                    old = bindingSet.getValue("part").toString();
                                
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Table")) {
                                
                                String table = bindingSet.getValue("text").toString().replace("@html", "");
                                //System.out.println(bindingSet.getValue("part").toString());
                                
                                if ((mod==0)|| (!table.contains("modification"))) {
                                    //System.out.println("NEW TABLE");
                                    if(legald.getChapters().isEmpty()){
                                        legald.getArticles().get(art_count).getParagraphs().get(count2).setTable(trimDoubleQuotes(table));
                                    }
                                    else{
                                        legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).setTable(trimDoubleQuotes(table));
                                    }
                                    //legald.getArticles().get(art_count).getParagraphs().get(count2).setTable(trimDoubleQuotes(table));
                                }
                                else if ((mod==1)||(mod==2)){
                                    //System.out.println("MODIFICATION TABLE");
                                    paragraph.setTable(trimDoubleQuotes(table));
                                    if(legald.getChapters().isEmpty()){
                                        if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                        }
                                        else{
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                        }
                                    }
                                    else{
                                        if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                        }
                                    }
                                }
                                else{
                                    article.getParagraphs().get(article.getParagraphs().size()-1).setTable(trimDoubleQuotes(table));
                                    if(legald.getChapters().isEmpty()){
                                        if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                        }
                                        else{
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                        }
                                    }
                                    else{
                                        if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                        }
                                    }
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Case")) {
                                
                                if (!old.equals(bindingSet.getValue("part").toString())) {
                                    
                                    Case case1 = new Case();
                                    
                                    case1.setURI(bindingSet.getValue("part").toString());
                                    Passage passage = new Passage();
                                    
                                    if (req==1&&bindingSet.getValue("html")!=null) {
                                        String text = bindingSet.getValue("html").toString().split("\\^\\^",2)[0];
                                        passage.setText(trimDoubleQuotes(text));
                                    }
                                    else{
                                        String text = bindingSet.getValue("text").toString().replace("@el", "");
                                        passage.setText(trimDoubleQuotes(text));
                                    }
                                    
                                    //System.out.println(case1.getURI());
                                    case1.getPassages().add(passage);
                                    String[] cases = case1.getURI().split("case");
                                    old = bindingSet.getValue("part").toString();
                                    //case1.setText(bindingSet.getValue("text").toString());

                                    if (cases.length > 2) {
                                        case1.setId(Integer.parseInt(case1.getURI().split("case\\/")[1]));
                                        //System.out.println("NEW CASE"+count4);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                            }
                                        }
                                    }
                                    else if (mod==0) {
                                        case1.setId(Integer.parseInt(case1.getURI().split("case\\/")[1]));
                                        //System.out.println("NEW CASE"+count4);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().add(case1);
                                            }
                                            else{
                                                legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().add(case1);
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().add(case1);
                                            }
                                            else{
                                                legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().add(case1);
                                            }
                                        }
                                        count4 ++;
                                    }
                                    else if (mod==1) {
                                        case1.setId(Integer.parseInt(case1.getURI().split("case\\/")[1]));
                                        //System.out.println("MODIFICATION CASE");
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(case1.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Case");
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(case1);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Case");
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(case1);
                                                }
                                            }
                                            else{
                                                if(case1.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Case");
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(case1);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Case");
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(case1);
                                                }
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(case1.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Case");
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(case1);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Case");
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(case1);
                                                }
                                            }
                                            else{
                                                if(case1.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setType("Case");
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(case1);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setType("Case");
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(case1);
                                                }
                                            }
                                        }
                                        mod=0;
                                        mod_count4 ++;
                                    }
                                    else if (mod==2) {
                                        case1.setId(Integer.parseInt(case1.getURI().split("modification")[1].split("case\\/")[1]));
                                        //System.out.println("PARAGPAPH MODIFICATION CASE");
                                        paragraph.getCaseList().add(case1);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                            else{
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                            else{
                                                if(paragraph.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(paragraph);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(paragraph);
                                                }
                                            }
                                        }
                                        //legald.getArticles().get(art_count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                        mod_count4 ++;
                                    }
                                    else{
                                        case1.setId(Integer.parseInt(case1.getURI().split("modification")[1].split("case\\/")[1]));
                                        //System.out.println("ARTICLE PARAGPAPH MODIFICATION CASE");
                                        article.getParagraphs().get(article.getParagraphs().size()-1).getCaseList().add(case1);
                                        if(legald.getParts().isEmpty()){
                                            if(legald.getChapters().isEmpty()){
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                            else{
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                        }
                                        else{
                                            if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                            else{
                                                if(article.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).getModification().setFragment(article);
                                                }
                                                else{
                                                    legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).getModification().setFragment(article);
                                                }
                                            }
                                        }
                                        //legald.getArticles().get(art_count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                        mod_count4 ++;
                                    }
                                    
                                }
                            
                            }
                            else if ((bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Edit")) || (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Addition")) ||(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Deletion"))) {
                                
                                Modification modification = new Modification();
                                modification.setURI(bindingSet.getValue("part").toString());
                                //System.out.println(modification.getURI());
                                //System.out.println("MODIFICATION");
                                modification.setType(bindingSet.getValue("type").toString());
                                if(legald.getParts().isEmpty()){
                                    if(legald.getChapters().isEmpty()){
                                        if(modification.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).setModification(modification);
                                        }
                                        else{
                                            legald.getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).setModification(modification);
                                        }
                                    }
                                    else{
                                        if(modification.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).setModification(modification);
                                        }
                                        else{
                                            legald.getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).setModification(modification);
                                        }
                                    }
                                }
                                else{
                                    if(legald.getParts().get(part_count).getChapters().isEmpty()){
                                        if(modification.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).setModification(modification);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).setModification(modification);
                                        }
                                    }
                                    else{
                                        if(modification.getURI().split("\\/[0-9]+\\/modification")[0].endsWith("passage")){
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getPassages().get(count3).setModification(modification);
                                        }
                                        else{
                                            legald.getParts().get(part_count).getChapters().get(chap_count).getArticles().get(art_count).getParagraphs().get(count2).getCaseList().get(count4).setModification(modification);
                                        }
                                    }
                                }
                                mod = 1;
                                mod_count3 = -1;
                                mod_count4 = -1;
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/ParsingIssue")) {
                                String text = bindingSet.getValue("text").toString().replace("@el", "");
                                legald.getIssues().add(trimDoubleQuotes(text));
                            }

                    }
                        
                }
                finally {
                    result.close();
                }
                
//                int view = Integer.getInteger(legald.getViews());
//                    
//                String queryString2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
//                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
//                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
//                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
//                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
//                "\n" +
//                "DELETE WHERE {\n" +
//                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
//                " <http://legislation.di.uoa.gr/ontology/views>" +
//                " \"" + view + "\"^^<http://www.w3.org/2001/XMLSchema#integer>\n" +
//                "}";
//                
//                //System.out.println(queryString2);
//                Update update = con.prepareUpdate(QueryLanguage.SPARQL, queryString2);
//                update.execute();
//                con.commit();
//                
//                view = view+1;
//                
//                String queryString3 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
//                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
//                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
//                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
//                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
//                "\n" +
//                "INSERT DATA {\n" +
//                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
//                " <http://legislation.di.uoa.gr/ontology/views>" +
//                " \"" + view + "\"^^<http://www.w3.org/2001/XMLSchema#integer>\n" +
//                "}";
//                
//                //System.out.println(queryString3);
//                Update update2 = con.prepareUpdate(QueryLanguage.SPARQL, queryString3);
//                update2.execute();
//                con.commit();
                
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        //sort containts of legald before displayiing it
        LegalDocumentSort srt = new LegalDocumentSort();
        
        //getHTMLById(decisionType, year, id, legald);
        return srt.sortld(legald);

    }
    
    @Override
    public String getRDFById(String decisionType, String year, String id) {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String result = "";
        
        try {
           
            RepositoryConnection con = repo.getConnection();
           
            try {
                
                String queryString = "DESCRIBE <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">";
                //System.out.println(queryString);
                  
                try {
                    // use SPARQL query
                    RDFXMLWriter writer = new RDFXMLWriter(out);
                    con.prepareGraphQuery(QueryLanguage.SPARQL,queryString).evaluate(writer); 
                    out.writeTo(System.out);
                    result = out.toString("ISO-8859-1");
                }
                catch (IOException ex) {
                    Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }                  
                finally {
                }
                  
            }
            finally {
                con.close();
            }
                 
        }
        catch (OpenRDFException e) {
            // handle exception
        }
         
        return result;
    
    }
    
    @Override
    public EndpointResultSet sparqlQuery(EndpointResultSet endpointResult,String format){
        String results = "";
       
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            //System.out.println("1");
            endpointResult.setMessage(e.toString());
            e.printStackTrace();
        }

        // connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            //System.out.println("2");
            endpointResult.setMessage(ex.toString());
        }
        
        if(endpointResult.getQuery().contains("DESCRIBE")){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
           RepositoryConnection con = repo.getConnection();
            try {
                // use SPARQL query
                RDFXMLWriter writer = new RDFXMLWriter(out);
                con.prepareGraphQuery(QueryLanguage.SPARQL,endpointResult.getQuery()).evaluate(writer); 
                out.writeTo(System.out);
                results += "<tr><td>Result</td></tr><tr><td>";
                results += "<pre>" ; 
                String xml = out.toString("UTF-8");
                xml = xml.replaceAll("<", "&lt");
                xml = xml.replaceAll(">", "&gt");
                results += xml+ "</pre>";
                results += "</td></tr>";
            }
            catch (IOException ex) {
                endpointResult.setMessage(ex.toString());
            }   catch (MalformedQueryException ex) {                  
                    endpointResult.setMessage(ex.toString());
                } catch (QueryEvaluationException ex) {
                    endpointResult.setMessage(ex.toString());
                } catch (RDFHandlerException ex) {
                    endpointResult.setMessage(ex.toString());
                }                  
            finally {
            }
            } catch (RepositoryException ex) {
               endpointResult.setMessage(ex.toString());
            }
        }
        else{
        
        if(format.equals("XML")){
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               try {
                   SPARQLResultsXMLWriter sparqlWriter = new SPARQLResultsXMLWriter(out);

                   RepositoryConnection con = repo.getConnection();
                   try {
                      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, endpointResult.getQuery());
                      tupleQuery.evaluate(sparqlWriter);
                      results += "<tr><td>Result</td></tr><tr><td>";
                      results += "<pre>" ; 
                      String xml = out.toString("UTF-8");
                      xml = xml.replaceAll("<", "&lt");
                      xml = xml.replaceAll(">", "&gt");
                      results += xml+ "</pre>";
                      results += "</td></tr>";
                    }
                    catch (IOException ex) {
                        endpointResult.setMessage(ex.toString());
                    }
                    catch (MalformedQueryException ex) {                  
                            endpointResult.setMessage(ex.toString());
                    } catch (QueryEvaluationException ex) {
                            endpointResult.setMessage(ex.toString());
                    } catch (RepositoryException ex) {
                            endpointResult.setMessage(ex.toString());
                    } catch (TupleQueryResultHandlerException ex) {      
                       endpointResult.setMessage(ex.toString());
                   }      
                   finally {
                      try{
                         con.close();
                      }
                      catch (RepositoryException ex) {
                            endpointResult.setMessage(ex.toString());
                      }
                   }
                }
            catch (RepositoryException ex) {
                Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }                finally {
                   try{
                   out.close();
                   }
                   catch (IOException ex) {
                        endpointResult.setMessage(ex.toString());
                    }
                }
        }
        else if(format.equals("HTML")){
            TupleQueryResult result;
             try {
               RepositoryConnection con = repo.getConnection();
               try {

                      //System.out.println(endpointResult.getQuery());
                      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, endpointResult.getQuery());
                      result = tupleQuery.evaluate();

                      try {
                        //iterate the result set

                        List<String> bindingNames = result.getBindingNames();
                        results += "<tr>";
                        for (String bindingName : bindingNames) {
                                results += "<td>" + bindingName + "</td>";
                        }
                        results += "</tr>";
                        while (result.hasNext()) {
                               BindingSet bindingSet = result.next();
                               results += "<tr>";
                            for (String bindingName : bindingNames) {
                                if(bindingSet.getValue(bindingName)!=null){
                                    results += "<td>" + bindingSet.getValue(bindingName).toString() + "</td>";
                                }
                                else{
                                    results += "<td></td>";
                                }
                            }
                               results += "</tr>";
                       }
                    }
                    finally {
                            result.close();
                    }

               }
               finally {
                  con.close();
               }
            }
            catch (OpenRDFException e) {
                //System.out.println("3");
               endpointResult.setMessage(e.toString());
               // handle exception
            }
        }
        
        }
        endpointResult.setResults(results);
        return endpointResult;

    }

    @Override
    public List<Modification> getAllModifications(String decisionType, String year, String id, String date, int req) {
         
        List<Modification> modifications = new ArrayList<Modification>();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        try {
            
            repo.initialize();
            
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT DISTINCT ?mod ?type ?patient ?work ?title ?date ?gaztitle ?part ?type2 ?text\n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:realizedBy  ?version.\n" +
                    " ?version metalex:matterOf ?mod.\n" +
                    " ?mod rdf:type ?type.\n" +
                    " ?mod metalex:patient ?patient.\n" +
                    " ?mod metalex:part+ ?part.\n" +
                    " ?part rdf:type ?type2.\n" +
                    " ?mod  metalex:legislativeCompetenceGround ?work.\n" +
                    " ?work leg:gazette ?gazette.\n" +
                    " ?work dc:created ?date.\n" +
                    " ?gazette dc:title ?gaztitle.\n" +
                    " OPTIONAL{?work  dc:title ?title.}\n" ;
                    
                    if (date !=null) {
                        
                        if (req == 1) {
                            queryString += "FILTER (?date <= \""+ date +"\"^^xsd:date)\n" +
                            "OPTIONAL{\n" +
                            " ?part leg:text ?text.\n" +
                            "}.}\n" +
                            "ORDER BY ?mod";
                        }
                        else if (req == 2) {
                            queryString += "FILTER (?date <= \""+ date +"\"^^xsd:date)\n" +        
                            "FILTER NOT EXISTS {FILTER(langMatches(lang(?text), \"html\"))}\n" +
                            "OPTIONAL{\n" +
                            " ?part leg:text ?text.\n" +
                            "}.}\n" +
                            "ORDER BY ?mod";
                        }
                        
                    }
                    else {
                        
                        if (req == 1) {
                            queryString += "OPTIONAL{\n" +
                            "?part leg:text ?text.\n" +
                            "}.}\n" +
                            "ORDER BY ?mod";
                        }
                        else if (req == 2) {
                            queryString += "FILTER NOT EXISTS {FILTER(langMatches(lang(?text), \"html\"))}\n" +
                            "OPTIONAL{\n" +
                            "?part leg:text ?text.\n" +
                            "}.}\n" +
                            "ORDER BY ?mod";
                        }
                        
                    }
                  
                    //System.out.println(queryString);
                    TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                    result = tupleQuery.evaluate();

                    try {
                        
                        // iterate the result set
                        int counter = -1;
                        int count = -1;
                        int count2 = -1;
                        int count3 = -1;
                        int count4 = -1;
                        int frag = 0;
                    
                        Fragment fragment = null;
                        Modification mod = null;
                    
                        String old = "old";
                        String current = "";
                        
                        while (result.hasNext()) {
                            
                            BindingSet bindingSet = result.next();
                            
                            if (!bindingSet.getValue("mod").toString().equals(current)) {
                                
                                if (mod != null) {
                                    mod.setFragment(fragment);
                                    modifications.add(mod);
                                }
                                
                                mod = new Modification();
                                mod.setURI(bindingSet.getValue("mod").toString());
                                mod.setType(bindingSet.getValue("type").toString());
                                mod.setPatient(bindingSet.getValue("patient").toString());
                                mod.getCompetenceGround().setURI(bindingSet.getValue("work").toString());
                                String gaztitle = bindingSet.getValue("gaztitle").toString().replace("^^", "");
                                mod.getCompetenceGround().setFEK(trimDoubleQuotes(gaztitle));
                                String date2 = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
                                mod.getCompetenceGround().setPublicationDate(trimDoubleQuotes(date2));
                                if(bindingSet.getValue("title")!=null){
                                    mod.getCompetenceGround().setTitle(trimDoubleQuotes(bindingSet.getValue("title").toString().replace("@el", "")));
                                }
                                current = bindingSet.getValue("mod").toString();
                                frag = 0;
                                
                            }

                            if (bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Article")) {
                                
                                Article article = new Article();
                                article.setId(Integer.toString(count+2));
                                article.setURI(bindingSet.getValue("part").toString());
                                //System.out.println(article.getURI());
                                //System.out.println("NEW ARTICLE");
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                fragment = article;
                                if(mod.getType().contains("Edit")){
                                    fragment.setStatus(2);
                                }
                                else{
                                    fragment.setStatus(1);
                                }
                                fragment.setType("Article");
                                frag = 1;
                                
                            }
                            else if (bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")) {
                                
                                Paragraph paragraph = new Paragraph();
                                paragraph.setId(Integer.toString(count2+2));
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                //System.out.println(paragraph.getURI());
                                //System.out.println("NEW PARAGRAPH");
                                
                                if(frag == 0) {
                                    fragment = paragraph;
                                    if(mod.getType().contains("Edit")){
                                        fragment.setStatus(2);
                                    }
                                    else{
                                        fragment.setStatus(1);
                                    }
                                    fragment.setType("Paragraph");
                                    frag = 2;
                                }
                                else{
                                    Article article = (Article) fragment;
                                    article.getParagraphs().add(paragraph);
                                    fragment = article;
                                }
                                
                                count2++;
                                count3 = -1;
                                count4 = -1;
                                
                            }
                            else if (bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")) {
                                    
                                Passage passage = new Passage();
                                passage.setId(count3+2);
                                passage.setURI(bindingSet.getValue("part").toString());

                                if (bindingSet.getValue("text").toString().contains("@html")) {
                                    String text = bindingSet.getValue("text").toString().replace("@html", "");
                                    passage.setText(trimDoubleQuotes(text));
                                }
                                else if (bindingSet.getValue("text").toString().contains("@el")) {
                                    String text = bindingSet.getValue("text").toString().replace("@el", "");
                                    passage.setText(trimDoubleQuotes(text));
                                }

                                //System.out.println(passage.getURI());
                                //System.out.println("NEW PASSAGE");

                                if(frag == 0) {
                                    fragment = passage;
                                    if(mod.getType().contains("Edit")){
                                        fragment.setStatus(2);
                                    }
                                    else{
                                        fragment.setStatus(1);
                                    }
                                    fragment.setType("Passage");
                                }
                                else if (frag == 1) {
                                    Article article = (Article) fragment;
                                    article.getParagraphs().get(count2).getPassages().add(passage);
                                    fragment = article;
                                }
                                else if (frag == 2) {
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getPassages().add(passage);
                                    fragment = paragraph;
                                }
                                else {
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getPassages().add(passage);
                                    fragment = paragraph;
                                }

                                count3 ++;
                                
                            }
                            else if (bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Case")) {
                                
                                Case case1 = new Case();
                                case1.setId(count4+2);
                                case1.setURI(bindingSet.getValue("part").toString());
                                Passage passage = new Passage();

                                if (bindingSet.getValue("text").toString().contains("@html")) {
                                    String text = bindingSet.getValue("text").toString().replace("@html", "");
                                    passage.setText(trimDoubleQuotes(text));
                                }
                                else if (bindingSet.getValue("text").toString().contains("@el")) {
                                    String text = bindingSet.getValue("text").toString().replace("@el", "");
                                    passage.setText(trimDoubleQuotes(text));
                                }

                                case1.getPassages().add(passage);
                                //System.out.println(case1.getURI());
                                //System.out.println("NEW CASE");

                                if(frag == 0) {
                                    fragment = case1;
                                    if(mod.getType().contains("Edit")){
                                        fragment.setStatus(2);
                                    }
                                    else{
                                        fragment.setStatus(1);
                                    }
                                    fragment.setType("Case");
                                }
                                else if (frag == 1) {
                                    Article article = (Article) fragment;
                                    article.getParagraphs().get(count2).getCaseList().add(case1);
                                    fragment = article;
                                }
                                else if (frag == 2) {
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getCaseList().add(case1);
                                    fragment = paragraph;
                                }
                                else {
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getCaseList().add(case1);
                                    fragment = paragraph;
                                }

                                count4 ++;
                                
                            }

                        }
                        if (mod != null) {
                            mod.setFragment(fragment);
                            modifications.add(mod);
                        }
                        
                    }
                    finally {
                        result.close();
                    }
                 
                }
                finally {
                    con.close();
                }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        /*for(int i =0; i< modifications.size(); i++) {

            System.out.println(modifications.get(i).getURI());
            System.out.println(modifications.get(i).getType());
            System.out.println(modifications.get(i).getPatient());
            
        }*/

        return modifications;
            
    }
   
    @Override
    public List<LegalDocument> search(Map<String, String> params) {
        List<LegalDocument> LDs = new ArrayList<LegalDocument>();
        //Apache Lucene searching via criteria
        try {
            Path path = Paths.get("/storage/nomothesia/apache-tomcat-7.0.61/webapps/nomothesia-1.0.0/WEB-INF/classes/fek_index");//home/kiddo/NetBeansProjects/nomothesia-api/src/main/resources/fek_index");//getClass().getResource("/fek_index").toString());
            Directory directory2 = FSDirectory.open(path);       
            IndexReader indexReader =  DirectoryReader.open(directory2);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            BooleanQuery finalQuery = new BooleanQuery();
            if(params.get("keywords")!=null&&!params.get("keywords").isEmpty()){
                if(params.get("keywords").contains(",")){
                    BooleanQuery multipleKeyWords = new BooleanQuery();
                    multipleKeyWords.setMinimumNumberShouldMatch(1);
                    String[] keywords = params.get("keywords").split(",");
                    for (String keyword : keywords) {
                        QueryParser queryParser = new QueryParser("text",  new StandardAnalyzer());
                        Query query = queryParser.parse("\"" + keyword.trim() + "\"~");
                        multipleKeyWords.add(query, Occur.SHOULD);
                    }
                    finalQuery.add(multipleKeyWords, Occur.MUST);
                }
                if(params.get("keywords").contains(" ")){
                    BooleanQuery multipleKeyWords = new BooleanQuery();
                    multipleKeyWords.setMinimumNumberShouldMatch(1);
                    String[] keywords = params.get("keywords").split("( )+");
                    for (String keyword : keywords) {
                        QueryParser queryParser = new QueryParser("text",  new StandardAnalyzer());
                        Query query = queryParser.parse("\"" + keyword.trim() + "\"~");
                        multipleKeyWords.add(query, Occur.SHOULD);
                    }
                    finalQuery.add(multipleKeyWords, Occur.MUST);
                }
                else{
                    QueryParser queryParser = new QueryParser("text",  new StandardAnalyzer());
                    Query query = queryParser.parse("\""+params.get("keywords")+"\"~");
                    finalQuery.add(query, Occur.MUST);
                }
            }
            if(params.get("year")!=null&&!params.get("year").isEmpty()){
                Query query2 = NumericRangeQuery.newIntRange("year", 1, Integer.parseInt(params.get("year")), Integer.parseInt(params.get("year")), true, true);//queryParser2.parse(params.get("year"));
                finalQuery.add(query2, Occur.MUST);
            }
            if(params.get("fek_year")!=null&&!params.get("fek_year").isEmpty()){
                Query query3 = NumericRangeQuery.newIntRange("year", 1, Integer.parseInt(params.get("fek_year")), Integer.parseInt(params.get("fek_year")), true, true);//queryParser3.parse(params.get("fek_year"));
                finalQuery.add(query3, Occur.MUST);
            }
            if(params.get("fek_id")!=null&&!params.get("fek_id").isEmpty()){
                Query query4 = NumericRangeQuery.newIntRange("fek_id", 1, Integer.parseInt(params.get("fek_id")), Integer.parseInt(params.get("fek_id")), true, true);//queryParser4.parse(params.get("fek_id"));
                finalQuery.add(query4, Occur.MUST);
            }
            if(params.get("type")!=null&&!params.get("type").isEmpty()){
                if(params.get("type").contains(",")){
                    BooleanQuery multipleTypes = new BooleanQuery();
                    multipleTypes.setMinimumNumberShouldMatch(1);
                    String[] types = params.get("type").split(",");
                    for (String type : types) {
                        multipleTypes.add(new TermQuery(new Term("type", type)), Occur.SHOULD);
                    }
                    finalQuery.add(multipleTypes, Occur.MUST);
                }
                else{
                    QueryParser queryParser5 = new QueryParser("type",  new StandardAnalyzer());
                    Query query5 = queryParser5.parse(params.get("type"));
                    finalQuery.add(query5, Occur.MUST);
                }
            }
            if(params.get("id")!=null&&!params.get("id").isEmpty()){
                Query query6 = NumericRangeQuery.newLongRange("numeric_id", 1, Long.parseLong(params.get("id")), Long.parseLong(params.get("id")), true, true);//queryParser6.parse(params.get("id"));
                finalQuery.add(query6, Occur.MUST);
            }
            if(params.get("date")!=null&&!params.get("date").isEmpty()){
                Query query6 = NumericRangeQuery.newIntRange("date", 1, Integer.parseInt(params.get("date").replace("-", "")), Integer.parseInt(params.get("date").replace("-", "")), true, true);//queryParser6.parse(params.get("id"));
                finalQuery.add(query6, Occur.MUST);
            }
            if(params.get("datefrom")!=null&&!params.get("datefrom").isEmpty()){
                Query query6 = NumericRangeQuery.newIntRange("date", 1, Integer.parseInt(params.get("datefrom").replace("-", "")), Integer.parseInt(params.get("dateto").replace("-", "")), true, true);//queryParser6.parse(params.get("id"));
                finalQuery.add(query6, Occur.MUST); 
            }
            TopDocs topDocs = indexSearcher.search(finalQuery,300);
            if(topDocs.totalHits>0){ //
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) { 
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    
                    LegalDocument ld = new LegalDocument();
                    ld.setURI(document.get("uri"));
                    ld.setFEK(document.get("fek"));
                    ld.setPublicationDate(document.get("date").substring(0, 4)+"-"+document.get("date").substring(4,6)+"-"+document.get("date").substring(6,8));
                    ld.setDecisionType(document.get("type"));
                    ld.setYear(document.get("year"));
                    ld.setId(document.get("id"));
                    String title = trimDoubleQuotes(document.get("title"));
                    if(title.length()>400){
                        title = title.substring(0, 350) + "[...]";
                    }
                    ld.setTitle(title);
                    LDs.add(ld);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return LDs;
    }
//    
//     @Override
//    public List<LegalDocument> search(Map<String, String> params) {
//        
//        /*for (Map.Entry entry : params.entrySet()) {
//            System.out.println(entry.getKey() + "" + entry.getValue());
//        }*/
//        List<LegalDocument> LDs = new ArrayList<LegalDocument>();
//        
//        if(!params.isEmpty()){
//
//        String sesameServer ="";
//        String repositoryID ="";
//        
//        Properties props = new Properties();
//        InputStream fis = null;
//        
//        try {
//            
//            fis = getClass().getResourceAsStream("/properties.properties");
//            props.load(fis);
//            
//            // get the properties values
//            sesameServer = props.getProperty("SesameServer");
//            repositoryID = props.getProperty("SesameRepositoryID");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Connect to Sesame
//        Repository repo = new HTTPRepository(sesameServer, repositoryID);
//        
//        try {
//            repo.initialize();
//        } catch (RepositoryException ex) {
//            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        TupleQueryResult result;
//         
//        try {
//           
//            RepositoryConnection con = repo.getConnection();
//                
//                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
//                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
//                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
//                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
//                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
//                "\n" +
//                "SELECT DISTINCT ?legaldocument ?title ?type ?date ?id\n" +
//                "WHERE{\n" +
//                " ?legaldocument dc:title ?title.\n";
//                  
//                //search by specific date
//                if((params.get("date")==null) || (params.get("date").equals(""))){
//                    queryString += "?legaldocument dc:created ?date.\n";
//                }
//                else if ((params.get("year")==null) || (params.get("year").equals(""))){
//                    queryString += "?legaldocument dc:created ?date.\n" + 
//                    "FILTER (?date = \"" + params.get("date") + "\"^^xsd:date)\n";
//                }
//                  
//                //search between two dates
//                if ((params.get("datefrom")==null) || (params.get("datefrom").equals(""))) {
//
//                }
//                else if((params.get("year")==null) || (params.get("year").equals(""))) {
//                    queryString += "FILTER ( ?date >= \"" + params.get("datefrom") + "\"^^xsd:date)\n";
//                }
//
//                if ((params.get("dateto")==null) || (params.get("dateto").equals(""))) {
//
//                }
//                else if ((params.get("year")==null) || (params.get("year").equals(""))){
//                    queryString += "FILTER ( ?date <= \"" + params.get("dateto") + "\"^^xsd:date)\n";
//                }
//                  
//                //search by year
//                if ((params.get("year")==null) || (params.get("year").equals(""))) {
//
//                }
//                else if (((params.get("date")==null) || (params.get("date").equals(""))) && ((params.get("datefrom")==null) || (params.get("datefrom").equals(""))) && ((params.get("dateto")==null) || (params.get("dateto").equals("")))) {
//                    queryString += "FILTER ( ?date >= \"" + params.get("year") + "-01-01\"^^xsd:date && ?date <= \"" + params.get("year") + "-12-30\"^^xsd:date)\n";
//                }
//                  
//                //search by fek year, issue and id
//                if (((params.get("fek_year")==null) || (params.get("fek_year").equals(""))) && ((params.get("fek_id")==null) || (params.get("fek_id").equals("")))) {
//
//                }
//                else {
//                    queryString += "?legaldocument leg:gazette ?gazette.\n" +
//                    "?gazette dc:title \"" + "Α/" + params.get("fek_year") + "/" + params.get("fek_id") + "\".\n";
//                }
//                
//                //search by legal document id
//                if((params.get("id")==null) || (params.get("id").equals(""))){
//                    queryString += "?legaldocument leg:legislationID ?id.\n";
//                }
//                else {
//                    queryString += "?legaldocument leg:legislationID \""+ params.get("id") +"\"^^xsd:integer.\n";
//                }
//                  
//                //search by type
//                if((params.get("type")==null) || (params.get("type").equals(""))){
//                    queryString += " ?legaldocument rdf:type ?type.\n";
//                }
//                else {
//                    String type =  params.get("type");
//                    List<String> items = Arrays.asList(type.split("\\s*,\\s*"));
//                    queryString += " ?legaldocument rdf:type ?type.\n";
//                    if (items.size()>1) {
//                          
//                        queryString += "?legaldocument rdf:type ?type2.\n";
//                        queryString += "FILTER(";
//                          
//                        for (int i=0;i<items.size();i++) {
//                              
//                            if (i == items.size()-1) {
//                                  
//                                if (items.get(i).equals("con")) {
//                                    queryString += "?type2=leg:Constitution";    
//                                }
//                                else if (items.get(i).equals("pd")) {
//                                    queryString += "?type2=leg:PresidentialDecree";
//                                }
//                                else if (items.get(i).equals("law")) {
//                                    queryString += "?type2=leg:Law";
//                                }
//                                else if (items.get(i).equals("amc")) {
//                                    queryString += "?type2=leg:ActOfMinisterialCabinet";
//                                  }
//                                else if (items.get(i).equals("md")) {
//                                    queryString += "?type2=leg:MinisterialDecision";
//                                }
//                                else if (items.get(i).equals("rd")) {
//                                    queryString += "?type2=leg:RoyalDecree";
//                                }
//                                else if (items.get(i).equals("la")) {
//                                    queryString += "?type2=leg:LegislativeAct";
//                                }
//                                else if (items.get(i).equals("rp")){
//                                    queryString += "?type2=leg:RegulatoryProvision";
//                                }
//                                  
//                            }
//                            else {
//                                  
//                                if (items.get(i).equals("con")) {
//                                    queryString += "?type2=leg:Constitution || ";    
//                                }
//                                else if (items.get(i).equals("pd")) {
//                                    queryString += "?type2=leg:PresidentialDecree || ";
//                                }
//                                else if (items.get(i).equals("law")) {
//                                    queryString += "?type2=leg:Law || ";
//                                }
//                                else if (items.get(i).equals("amc")) {
//                                    queryString += "?type2=leg:ActOfMinisterialCabinet || ";
//                                }
//                                else if (items.get(i).equals("md")) {
//                                    queryString += "?type2=leg:MinisterialDecision || ";
//                                }
//                                else if (items.get(i).equals("rd")) {
//                                    queryString += "?type2=leg:RoyalDecree || ";
//                                }
//                                else if (items.get(i).equals("la")) {
//                                    queryString += "?type2=leg:LegislativeAct || ";
//                                }
//                                 else if (items.get(i).equals("rp")){
//                                    queryString += "?type2=leg:RegulatoryProvision || ";
//                                }
//                                  
//                            }
//                              
//                        }
//                          
//                        queryString += " )\n";
//                          
//                    }
//                    else {
//                        if (items.get(0).equals("con")) {
//                            queryString += "?legaldocument rdf:type leg:Constitution.\n";  
//                        }
//                        else if (items.get(0).equals("pd")) {
//                            queryString += "?legaldocument rdf:type leg:PresidentialDecree.\n";  
//                        }
//                        else if (items.get(0).equals("law")) {
//                            queryString += "?legaldocument rdf:type leg:Law.\n";  
//                        }
//                        else if (items.get(0).equals("amc")) {
//                            queryString += "?legaldocument rdf:type leg:ActOfMinisterialCabinet.\n";  
//                        }
//                        else if (items.get(0).equals("md")) {
//                            queryString += "?legaldocument rdf:type leg:MinisterialDecision.\n";  
//                        }
//                        else if (items.get(0).equals("rd")) {
//                            queryString += "?legaldocument rdf:type leg:RoyalDecree.\n";  
//                        }
//                        else if (items.get(0).equals("la")) {
//                            queryString += "?legaldocument rdf:type leg:LegislativeAct.\n";  
//                        }
//                        else if (items.get(0).equals("rp")){
//                            queryString += "?legaldocument rdf:type leg:RegulatoryProvision. \n";
//                        }
//                    
//                    }
//
//                }
//                
//                //search by keyword
//                if((params.get("keywords")!=null) && !params.get("keywords").equals("")) {
//                    
//                    // Tokenize keywords field
//                    String[] tokens;
//                    if(params.get("keywords").contains(",")){
//                        tokens = params.get("keywords").split(",");
//                    }
//                    else{
//                        tokens = params.get("keywords").split(" ");
//                    }
//                    //List<String> stopWords = Arrays.asList("Î“â€•","Î“Â§","Î“Î„Î“â€•","Î“â€•Î“Â©","Î“Î„Î“Ê½","Î“Î„Î“â€•Î“Î…","Î“Î„Î“Â§Î“Â²","Î“Î„Î“Î‰Î“Â­","Î“Î„Î“â€•Î“Â­","Î“Î„Î“Â§Î“Â­","Î“ïŸ„Î“Ê½Î“Â©","Î“ïŸ„Î“Â©","Î“ïŸ„","Î“ïŸƒÎ“ÂŸÎ“Â¬Î“Ê½Î“Â©","Î“ïŸƒÎ“ÂŸÎ“Â³Î“Ê½Î“Â©","Î“ïŸƒÎ“ÂŸÎ“Â­Î“Ê½Î“Â©","Î“ïŸƒÎ“ÂŸÎ“Â¬Î“Ê½Î“Â³Î“Î„Î“ïŸƒ","Î“ïŸƒÎ“ÂŸÎ“Â³Î“Î„Î“ïŸƒ","Î“Â³Î“Î„Î“â€•","Î“Â³Î“Î„Î“â€•Î“Â­","Î“Â³Î“Î„Î“Â§","Î“Â³Î“Î„Î“Â§Î“Â­","Î“Â¬Î“Ê½" ,"Î“Ê½Î“Â«Î“Â«Î“Âœ","Î“Ê½Î“Â°Î“ÎŒ","Î“Â£Î“Â©Î“Ê½","Î“Â°Î“Â±Î“â€•Î“Â²","Î“Â¬Î“ïŸƒ","Î“Â³Î“ïŸƒ","Î“Î‰Î“Â²","Î“Â°Î“Ê½Î“Â±Î“Âœ","Î“Ê½Î“Â­Î“Î„Î“ÂŸ","Î“ïŸ„Î“Ê½Î“Î„Î“Âœ","Î“Â¬Î“ïŸƒÎ“Î„Î“Âœ","Î“Â¨Î“Ê½","Î“Â­Î“Ê½","Î“ïŸ‚Î“ïŸƒ","Î“ïŸ‚Î“ïŸƒÎ“Â­","Î“Â¬Î“Â§","Î“Â¬Î“Â§Î“Â­","Î“ïŸƒÎ“Â°Î“Â©","Î“ïŸƒÎ“Â­Î“ÎŽ","Î“ïŸƒÎ“ÂœÎ“Â­","Î“Ê½Î“Â­","Î“Î„Î“ÎŒÎ“Î„Î“ïŸƒ","Î“Â°Î“â€•Î“Î…" ,"Î“Â°Î“Î‰Î“Â²" ,"Î“Â°Î“â€•Î“Â©Î“ÎŒÎ“Â²" ,"Î“Â°Î“â€•Î“Â©Î“Âœ","Î“Â°Î“â€•Î“Â©Î“ÎŒ","Î“Â°Î“â€•Î“Â©Î“â€•Î“Â©","Î“Â°Î“â€•Î“Â©Î“ïŸƒÎ“Â²","Î“Â°Î“â€•Î“Â©Î“Î‰Î“Â­","Î“Â°Î“â€•Î“Â©Î“â€•Î“Î…Î“Â²","Î“Ê½Î“Î…Î“Î„Î“ÎŒÎ“Â²","Î“Ê½Î“Î…Î“Î„Î“Âž","Î“Ê½Î“Î…Î“Î„Î“ÎŒ","Î“Ê½Î“Î…Î“Î„Î“â€•Î“ÂŸ","Î“Ê½Î“Î…Î“Î„Î“ÎŽÎ“Â­","Î“Ê½Î“Î…Î“Î„Î“â€•Î“Â½Î“Â²","Î“Ê½Î“Î…Î“Î„Î“Â?Î“Â²","Î“Ê½Î“Î…Î“Î„Î“Âœ","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“â€•Î“Â²","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“Â§","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“â€•","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“â€•Î“Â©","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“ïŸƒÎ“Â²","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“Ê½","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“Î‰Î“Â­","Î“ïŸƒÎ“ïŸ„Î“ïŸƒÎ“ÂŸÎ“Â­Î“â€•Î“Î…Î“Â²","Î“ÎŒÎ“Â°Î“Î‰Î“Â²","Î“ÎŒÎ“Â¬Î“Î‰Î“Â²","Î“ÂŸÎ“Â³Î“Î‰Î“Â²","Î“ÎŒÎ“Â³Î“â€•","Î“ÎŒÎ“Î„Î“Â©");
//                    List<String> keywords = new ArrayList<String>();
//                    
//                    // Stop Words Filtering
//                    for (String token : tokens) {
//                        //if ((!stopWords.contains(token)) || token.equals("")) {
//                            keywords.add(token.trim());
//                        //}
//                    }
//                    // Stemming
////                    for(int i=0; i< keywords.size(); i++){
////                        int size;
////                        if(keywords.get(i).length() <=5){
////                            size = keywords.get(i).length();
////                        }
////                        else if (keywords.get(i).length() <= 8){
////                            size = (int) (keywords.get(i).length() * 0.95);
////                        }
////                        else{
////                            size = (int) (keywords.get(i).length() * 0.8);
////                        }
////                        keywords.set(i,keywords.get(i).substring(0, size-1));
////                        System.out.println(keywords.get(i));
////                    }
//                    
//                    // Add regex filtering
//                    queryString += " ?legaldocument metalex:part+ ?part.\n";
//                    queryString += " ?part leg:text ?text.\n";
//                    queryString += " ?legaldocument leg:tag ?tag.\n";
//                    queryString += "FILTER( ";
//                    for(String keyword : keywords){
//                        queryString += "regex(?text, \"" + keyword +"\") || regex(?title,\"" + keyword +"\") || " +"regex(?tag,\"" + keyword +"\") || ";
//                    }
//                    queryString += "FALSE )\n";
//                }
//                  
//                queryString += "}";
//                
//                //System.out.println(queryString);
//                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//                result = tupleQuery.evaluate();
//
//                try {
//                    
//                    //iterate the result set
//                    while (result.hasNext()) {
//                        
//                        BindingSet bindingSet = result.next();
//                        LegalDocument ld = new LegalDocument();
//                       
//                        if ((params.get("date")==null) || (params.get("date").equals(""))) {
//                            String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
//                            //String date = bindingSet.getValue("date").toString();
//                            date = trimDoubleQuotes(date);
//                            ld.setPublicationDate(date);
//                            String[] year = date.split("-");
//                            ld.setYear(year[0]);
//                        }
//                        else {
//                            String date = params.get("date");
//                            ld.setPublicationDate(date);
//                            String[] year = date.split("-");
//                            ld.setYear(year[0]);
//                        }
//                        if ((params.get("id")==null) || (params.get("id").equals(""))) {
//                            String[] ids = bindingSet.getValue("id").toString().split("^^");
//                            String id = ids[0].replace("^^", "").replace("\"", "");
//                            ld.setId(trimDoubleQuotes(id));
//                        }
//                        else {
//                            String id = params.get("id");
//                            ld.setId(trimDoubleQuotes(id));
//                        }
//                        String type = bindingSet.getValue("type").toString();
//
//                        if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
//                            ld.setDecisionType("con");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
//                            ld.setDecisionType("pd");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
//                            ld.setDecisionType("law");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
//                            ld.setDecisionType("amc");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
//                            ld.setDecisionType("md");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
//                            ld.setDecisionType("rd");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
//                            ld.setDecisionType("la");
//                        }
//                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RegulatoryProvision")) {
//                            ld.setDecisionType("rp");
//                        }
//                        String title = "";
//                        if(bindingSet.getValue("title")!=null){
//                            title = bindingSet.getValue("title").toString().replace("@el", "");
//                        }
//                        else{
//                            title = ld.getDecisionType() + "" + ld.getYear() + " " + ld.getId();
//                        }
//                        ld.setTitle(trimDoubleQuotes(title));
//                        //String[] URIs = bindingSet.getValue("legaldocument").toString().split("uoa.gr/");
//                        ld.setURI(trimDoubleQuotes(bindingSet.getValue("legaldocument").toString()));
//                        LDs.add(ld);
//                   
//                    }
//                
//                }
//                finally {
//                    result.close();
//                }
//            
//        }
//        catch (OpenRDFException e) {
//            // handle exception
//        }
//        }
//        
//        return LDs;
//        
//    }
    
    @Override
    public List<String> getTags() {
        
        List<String> tags = new ArrayList<String>();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
           
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT DISTINCT ?tag \n" +
                "WHERE{\n" +
                "?legaldocument leg:tag ?tag.\n" +
                "}\n" +
                "ORDER BY ?tag";
                
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        String tag = bindingSet.getValue("tag").toString().replace("@el", "");
                        tags.add(trimDoubleQuotes(tag));
                    }
                    
                }
                finally {
                    result.close();
                }    
            }
            finally {
                con.close();
            }
        }
        catch (OpenRDFException e) {
            // handle exception
        }

        return tags;

    }
    
    @Override
    public List<LegalDocument> getViewed() {
        
        List<LegalDocument> legalviewed = new ArrayList<LegalDocument>();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT ?uri ?title ?date ?type ?views ?id\n" +
                "WHERE{\n" +
                "?uri dc:title ?title.\n" +
                "?uri dc:created ?date.\n" +
                "?uri leg:views ?views.\n" +
                "?uri rdf:type ?type.\n" +
                "?uri leg:legislationID ?id.\n" +
                "FILTER(langMatches(lang(?title), \"el\"))\n" +
                "}\n" +
                "ORDER BY ASC(?views)\n" +
                "LIMIT 6";
                  
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        LegalDocument legald = new LegalDocument();
                        //String[] URIs = bindingSet.getValue("uri").toString().split("uoa.gr/");
                        legald.setURI(trimDoubleQuotes(bindingSet.getValue("uri").toString()));
                        String title = "";
                        if(bindingSet.getValue("title")!=null){
                            title = bindingSet.getValue("title").toString().replace("@el", "");
                        }
                        else{
                            title = legald.getDecisionType() + "" + legald.getYear() + " " + legald.getId();
                        }
                        if(title.length()>200){
                            title = title.substring(0, 150) + "[...]\"";
                        }
                        legald.setTitle(trimDoubleQuotes(title));
                        String id = bindingSet.getValue("id").toString().replace("^^", "").replace("\"", "");
                        legald.setId(trimDoubleQuotes(id));
                        String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");  
                        date = trimDoubleQuotes(date);
                        legald.setPublicationDate(date);
                        String[] year = date.split("-");
                        legald.setYear(year[0]);
                        
                        String type = bindingSet.getValue("type").toString();
                           
                        if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                            legald.setDecisionType("con");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                            legald.setDecisionType("pd");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                            legald.setDecisionType("law");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                            legald.setDecisionType("amc");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                            legald.setDecisionType("md");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                            legald.setDecisionType("rd");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                            legald.setDecisionType("la");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RegulatoryProvision")) {
                            legald.setDecisionType("rp");
                        }
                            
                        legalviewed.add(legald);
                        
                    }
                    
                }
                finally {
                    result.close();
                }
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        return legalviewed;

    }
    
    @Override
    public String getLegislationTypeByYear() {
        
        List<LegalDocument> legalviewed = new ArrayList<LegalDocument>();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT ?uri ?date ?type\n" +
                "WHERE{\n" +
                "?uri dc:title ?title.\n" +
                "?uri dc:created ?date.\n" +
                "?uri rdf:type ?type.\n" +
                "FILTER(langMatches(lang(?title), \"el\"))\n" +
                "}\n" +
                "ORDER BY DESC(?views)\n" +
                "LIMIT 10";
                  
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        // TO DO
                    }
                    
                }
                finally {
                    result.close();
                }
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        return "";

    }
    
    @Override
    public List<LegalDocument> getRecent() {
        
        List<LegalDocument> legalrecent = new ArrayList<LegalDocument>();
        String sesameServer ="";
        String repositoryID ="";
        
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "\n" +
                "SELECT ?uri ?title ?date ?type ?views ?id\n" +
                "WHERE{\n" +
                "?uri dc:title ?title.\n" +
                "?uri dc:created ?date.\n" +
                "?uri leg:views ?views.\n" +
                "?uri rdf:type ?type.\n" +
                "?uri leg:legislationID ?id.\n" +
                "FILTER(langMatches(lang(?title), \"el\"))\n" +
                "}\n" +
                "ORDER BY DESC(?date)\n" +
                "LIMIT 10";
                  
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        LegalDocument legald = new LegalDocument();
                        //String[] URIs = bindingSet.getValue("uri").toString().split("uoa.gr/");
                        legald.setURI(trimDoubleQuotes(bindingSet.getValue("uri").toString()));
                        String title = "";
                        if(bindingSet.getValue("title")!=null){
                            title = bindingSet.getValue("title").toString().replace("@el", "");
                        }
                        else{
                            title = legald.getDecisionType() + "" + legald.getYear() + " " + legald.getId();
                        }
                        if(title.length()>200){
                            title = title.substring(0, 150) + "[...]\"";
                        }
                        legald.setTitle(trimDoubleQuotes(title));
                        String id = bindingSet.getValue("id").toString().replace("^^", "").replace("\"", "");
                        legald.setId(trimDoubleQuotes(id));
                        String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");  
                        date = trimDoubleQuotes(date);
                        legald.setPublicationDate(date);
                        String[] year = date.split("-");
                        legald.setYear(year[0]);
                        
                        String type = bindingSet.getValue("type").toString();
                           
                        if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                            legald.setDecisionType("con");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                            legald.setDecisionType("pd");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                            legald.setDecisionType("law");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                            legald.setDecisionType("amc");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                            legald.setDecisionType("md");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                            legald.setDecisionType("rd");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                            legald.setDecisionType("la");
                        }
                        else if (type.equals("http://legislation.di.uoa.gr/ontology/RegulatoryProvision")) {
                            legald.setDecisionType("rp");
                        }
                            
                        legalrecent.add(legald);
                        
                    }
                    
                }
                finally {
                    result.close();
                }
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        return legalrecent;

    }
    
    @Override
    public List<GovernmentGazette> getFEKStatistics() {
        
        List<GovernmentGazette> gazs= new ArrayList<GovernmentGazette>();
        
        String sesameServer ="";
        String repositoryID ="";
        String URI= "";
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                "SELECT ?gaz ?title ?pdf ?doc ?type ?date (COUNT (?doc) AS ?docs) (COUNT (?part) AS ?issues)\n" +
                "WHERE{\n" +
                "?gaz rdf:type leg:GovernmentGazette.\n" +
                "?gaz dc:title ?title.\n" +
                "?gaz dc:created ?date.\n" +
                "?gaz leg:pdfFile ?pdf.\n" +
                "    OPTIONAL {?doc leg:gazette ?gaz. ?doc rdf:type ?type.}\n" +
                "    OPTIONAL{?doc2 leg:gazette ?gaz. ?doc2 rdf:type ?type. ?doc2 metalex:part+ ?part. ?part rdf:type leg:ParsingIssue.}\n"+
                "}\n" +
                "GROUP BY ?gaz ?title ?pdf ?doc ?type ?date\n"+
                "ORDER BY ?gaz";
                  
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();
                GovernmentGazette gaz = new GovernmentGazette();
                ArrayList<LegalDocument> lds = new ArrayList<LegalDocument>();
                try {
                    
                    // iterate the result set
                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        if(!URI.equals(bindingSet.getValue("gaz").toString())){
                            gaz.setList(lds);
                            gazs.add(gaz);
                            gaz = new GovernmentGazette();
                            lds = new ArrayList<LegalDocument>();
                            gaz.setURI(bindingSet.getValue("gaz").toString());
                            //gaz.setId(trimDoubleQuotes(bindingSet.getValue("title").toString().split("^^")[0]).split("\\/")[2]);
                            URI = gaz.getURI();
                            String title = bindingSet.getValue("title").toString().replace("^^", "");
                            gaz.setTitle(trimDoubleQuotes(title));
                            String pdf = bindingSet.getValue("pdf").toString().replace("^^", "");
                            gaz.setFileName(trimDoubleQuotes(pdf));
                            String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
                            gaz.setPublicationDate(trimDoubleQuotes(date));
                            String docs = bindingSet.getValue("docs").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                            if(Integer.parseInt(trimDoubleQuotes(docs))!=0){
                                gaz.setDocs(1);//Integer.parseInt(trimDoubleQuotes(docs)));
                            }
                            String issues = bindingSet.getValue("issues").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                            gaz.setIssues(Integer.parseInt(trimDoubleQuotes(issues)));
                            LegalDocument ld = new LegalDocument();
                            if(gaz.getDocs()!=0){
                                String type = bindingSet.getValue("type").toString();
                                if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                                    ld.setDecisionType("con");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                                    ld.setDecisionType("pd");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                                    ld.setDecisionType("law");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                                    ld.setDecisionType("amc");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                                    ld.setDecisionType("md");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                                    ld.setDecisionType("rd");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                                    ld.setDecisionType("la");
                                }
                                else if (type.equals("http://legislation.di.uoa.gr/ontology/RegulatoryProvision")) {
                                    ld.setDecisionType("rp");
                                }
                                ld.setURI(bindingSet.getValue("doc").toString());
                                ld.setId(ld.getURI().split("gr\\/")[1].split("\\/",2)[1]);
                                lds.add(ld);
                            }
                        }
                        else{
                            LegalDocument ld = new LegalDocument();
                            String type = bindingSet.getValue("type").toString();
                            if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                                ld.setDecisionType("con");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                                ld.setDecisionType("pd");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                                ld.setDecisionType("law");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                                ld.setDecisionType("amc");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                                ld.setDecisionType("md");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                                ld.setDecisionType("rd");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                                ld.setDecisionType("la");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/RegulatoryProvision")) {
                                ld.setDecisionType("rp");
                            }
                            gaz.setDocs(gaz.getDocs()+1);
                            String issues = bindingSet.getValue("issues").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                            gaz.setIssues(gaz.getIssues() +Integer.parseInt(trimDoubleQuotes(issues)));
                            ld.setURI(bindingSet.getValue("doc").toString());
                            ld.setId(ld.getURI().split("gr\\/")[1].split("\\/",2)[1]);
                            lds.add(ld);
                        }
                    }
                }
                finally {
                    result.close();
                }
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        gazs.remove(0);
        //Collections.sort(gazs, new GGComparator());
        return gazs;

    }
    
    @Override
    public List<ArrayList<String>> getStatistics() {
        
        List<ArrayList<String>> lists= new ArrayList<ArrayList<String>>();

        String[] types = {"PresidentialDecree","Law","ActOfMinisterialCabinet","MinisterialDecision","RegulatoryProvision","LegislativeAct"};
        String[] years =  {"2006","2007","2008","2009","2010","2011","2012","2013","2014","2015"};
        String sesameServer ="";
        String repositoryID ="";
        String URI= "";
        Properties props = new Properties();
        InputStream fis = null;
        
        try {
            
            fis = getClass().getResourceAsStream("/properties.properties");
            props.load(fis);
            
            // get the properties values
            sesameServer = props.getProperty("SesameServer");
            repositoryID = props.getProperty("SesameRepositoryID");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TupleQueryResult result;
        
        try {
            
            RepositoryConnection con = repo.getConnection();
            
            try {
                for(int i=0; i< types.length; i++){
                    String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                                        "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                                        "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                                        "SELECT (COUNT (?doc) AS ?sum) ?year\n" +
                                        "WHERE{\n" +
                                        "?doc rdf:type leg:"+types[i]+".\n" +
                                        "?doc dc:created ?date.\n" +
                                        "BIND (year(?date) AS ?year).\n" +
                                        "}\n" +
                                        "GROUP BY ?year \n"+
                                        "ORDER BY ASC(?year)";

                    //System.out.println(queryString);
                    TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                    result = tupleQuery.evaluate();
                    try {
                        ArrayList<String> typed = new ArrayList<String>();
                        // iterate the result set
                        int k=0;
                        while (result.hasNext()) {
                            BindingSet bindingSet = result.next();  
                            if(bindingSet.getValue("year")!=null){
                                while(!trimDoubleQuotes(bindingSet.getValue("year").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>","")).equals(years[k])){
                                    typed.add("0");
                                    if(k<years.length-1){
                                        k++;
                                    }
                                    else{
                                        break;
                                    }
                                }
                                typed.add(trimDoubleQuotes(bindingSet.getValue("sum").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>","")));
                                k++;

                            }
                        }
                        lists.add(typed);
                    }
                    finally {
                        result.close();
                    }

                }
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }

        
        ArrayList<String> all = new ArrayList<String>();
        for(int i=0;i<10;i++){
            all.add("0");
        }
        //lists.remove(0);
        for(int i=0;i<lists.size();i++){
            for(int j=0; j<lists.get(i).size();j++){
                int sum = Integer.parseInt(all.get(j)) + Integer.parseInt(lists.get(i).get(j));
                all.set(j, Integer.toString(sum));
            }
        }
        lists.add(0, all);
        
        //Collections.sort(gazs, new GGComparator());
        return lists;

    }
    
    public static String trimDoubleQuotes(String text) {
        
        int textLength = text.length();
        
        if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
            return text.substring(1, textLength - 1);
        }
        
        return text;
    
    }
    
//    // HTTP GET request
//    private String getKML(String place) {
//
//            EndpointResult result = null;
//            Properties props = new Properties();
//            InputStream fis = null;
//            String host = "";
//            int port = 0;
//            String appName = "";
//            try {
//
//                fis = getClass().getResourceAsStream("/properties.properties");
//                props.load(fis);
//
//                // get the properties values
//                host = props.getProperty("StrabonHost");
//                port = Integer.parseInt(props.getProperty("StrabonPort"));
//                appName = props.getProperty("StrabonAppName");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String KML = "";
//            String query = "SELECT ?geo WHERE {<"+place+"> ?hasgeometry ?geo. ?hasgeometry <http://www.w3.org/2000/01/rdf-schema#label> \"has_geometry\"@en.}"; 
//
//            SPARQLEndpoint endpoint = new SPARQLEndpoint(host, port, appName);
//
//            try {
//                
//                result = endpoint.query(query, stSPARQLQueryResultFormat.KML);
//                
//                System.out.println("Status code: " + result.getStatusCode());
//                System.out.println("Query: " + query);
//                System.out.println("Status text: " + result.getStatusText());
//                KML = result.getResponse().replace("<?xml version='1.0' encoding='UTF-8'?>", "");
//                KML = KML.replaceAll("\n", "");
//                KML = KML.replaceAll("\r", "");
//                System.out.println("<----- Result ----->");
//                System.out.println(KML);
//                System.out.println("<----- Result ----->");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            
//            return KML;
//
//    }
    
}
