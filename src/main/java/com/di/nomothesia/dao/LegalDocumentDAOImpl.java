package com.di.nomothesia.dao;

import com.di.nomothesia.comparators.LegalDocumentSort;
import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.Citation;
import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.model.Paragraph;
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
import eu.earthobservatory.org.StrabonEndpoint.client.*;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.Update;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.query.resultio.stSPARQLQueryResultFormat;


public class LegalDocumentDAOImpl implements LegalDocumentDAO {

    @Override
    public LegalDocument getMetadataById(String decisionType, String year, String id) {
        
        LegalDocument legald = new LegalDocument();
        legald.setId(id);
        legald.setYear(year);
        
        if (decisionType.equals("pd")) {
            legald.setDecisionType("(��) ��������� ��������");
        }
        else if (decisionType.equals("law")) {
            legald.setDecisionType("�����");
        }
        else if(decisionType.equals("amc")){
            legald.setDecisionType("(���) ����� ���������� ����������");
        }
        else if(decisionType.equals("md")){
            legald.setDecisionType("(��) ��������� �������");
        }
        else if(decisionType.equals("rd")){
            legald.setDecisionType("(��) �������� ��������");
        }
        else if(decisionType.equals("la")){
            legald.setDecisionType("(���) ����� ����������� ������������");
        }
        
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
                "SELECT ?title ?date ?gaztitle ?signername ?signertitle ?views ?place\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " dc:title ?title.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " dc:created ?date.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:signer ?signer.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:gazette ?gazette.\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                " leg:views ?views.\n" +
                "?gazette dc:title ?gaztitle.\n" +
                "?signer foaf:name ?signername.\n" +
                "?signer foaf:title ?signertitle.\n" +
                "OPTIONAL{ <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +"> leg:place ?place.}"+
                "FILTER(langMatches(lang(?signername), \"el\"))\n"+
                "FILTER(langMatches(lang(?signertitle), \"el\"))\n"+
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
                        String title_el = bindingSet.getValue("title").toString().replace("@el", "");
                        legald.setTitle(trimDoubleQuotes(title_el));
                        String date2 = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
                        legald.setPublicationDate(trimDoubleQuotes(date2));
                        String fek = bindingSet.getValue("gaztitle").toString().replace("^^","");
                        legald.setFEK(trimDoubleQuotes(fek));
                        String name_el = bindingSet.getValue("signername").toString().replace("@el", "");
                        sign.setFullName(trimDoubleQuotes(name_el));
                        String signer_el = bindingSet.getValue("signertitle").toString().replace("@el", "");
                        sign.setTitle(trimDoubleQuotes(signer_el));
                        legald.getSigners().add(sign);
                        String views = bindingSet.getValue("views").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                        legald.setViews(trimDoubleQuotes(views));
                        
                        if((flag ==0) &&(bindingSet.hasBinding("place"))){
                            flag = 1;
                            legald.setPlace(this.getKML(bindingSet.getValue("place").toString()));
                        }
                    }
                    
                }
                catch (Exception ex) {
                    Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }                finally {
                    result.close();
                }
                
                
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
                 
            }
            finally {
                con.close();
            }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
       
        try {
            /*System.out.println(legald.getFEK());
            
            for(int i=0; i < legald.getSigners().size(); i++){
            System.out.println(legald.getSigners().get(i).getFullName() + " - " +legald.getSigners().get(i).getTitle());
            }
            
            for(int i=0; i < legald.gettags().size(); i++){
            System.out.println(legald.gettags().get(i));
            }*/
        } catch (Exception ex) {
            Logger.getLogger(LegalDocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
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
                "SELECT  DISTINCT ?citation ?cittext ?cituri\n" +
                "WHERE{\n" +
                "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +"> metalex:part ?citation.\n" +
                "?citation a metalex:BibliographicCitation.\n" +
                "OPTIONAL {?citation metalex:cites ?cituri.}.\n" +
                "?citation leg:context ?cittext.\n";
                
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
                    "SELECT DISTINCT ?part ?text ?type ?title\n" +
                    "WHERE{\n" +
                    "<http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:part+  ?part.\n" +
                    "?part rdf:type ?type.\n" +
                    "OPTIONAL{ ?part leg:text ?text.}.\n" +
                    "OPTIONAL{ ?part dc:title ?title.}.\n";
                            
                    if (req == 1) {        
                        queryString += "}\n" +
                        "ORDER BY ?part";
                    }
                    else if (req == 2) {
                        queryString += "FILTER NOT EXISTS {FILTER(langMatches(lang(?text), \"html\"))}\n" +
                        "}\n" +
                        "ORDER BY ?part";
                    }
                  
                    //System.out.println(queryString);
                    TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                    result = tupleQuery.evaluate();

                    try {
                    
                        // iterate the result set
                        int count = -1;
                        int count2 = -1;
                        int count3 = -1;
                        int count4 = -1;
                        int mod = 0;
                        
                        String old = "old";
                        Paragraph paragraph = null;
                    
                        while (result.hasNext()) {
                            
                            BindingSet bindingSet = result.next(); 
                            
                            if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Article")) {
                                
                                Article article = new Article();
                                article.setId(count+2);
                                article.setURI(bindingSet.getValue("part").toString());
                                
                                if (bindingSet.getValue("title")!=null) {
                                    String title = bindingSet.getValue("title").toString().replace("@el", "");
                                    article.setTitle(trimDoubleQuotes(title));
                                }
                                
                                //System.out.println(article.getURI());
                                //System.out.println("NEW ARTICLE");
                                legald.getArticles().add(article);
                                count ++;
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                mod = 0;
                            
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")) {
                                
                                paragraph = new Paragraph();
                                paragraph.setId(count2+2);
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                //System.out.println(paragraph.getURI());
                                
                                if ((mod==0)||(mod==2)) {
                                    //System.out.println("NEW PARAGRAPH");
                                    legald.getArticles().get(count).getParagraphs().add(paragraph);
                                    count2++;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                }
                                else {
                                    //System.out.println("MODIFICATION PARAGRAPH");
                                    mod = 2;
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Paragraph");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")) {
                                
                                if (!old.equals(bindingSet.getValue("part").toString())) {
                                    
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

                                    if ((mod==0)|| (!passage.getURI().contains("modification"))) {
                                        //System.out.println("NEW PASSAGE");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getPassages().add(passage);
                                    }
                                    else if (mod==1) {
                                        //System.out.println("MODIFICATION PASSAGE");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Passage");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(passage);
                                        mod =0;
                                    }
                                    else {
                                        //System.out.println("PARAGRAPH MODIFICATION PASSAGE");
                                        paragraph.getPassages().add(passage);
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                    }

                                    count3 ++;
                                    old = bindingSet.getValue("part").toString();
                                
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Table")) {
                                
                                String table = bindingSet.getValue("text").toString().replace("@html", "");
                                //System.out.println(bindingSet.getValue("part").toString());
                                
                                if ((mod==0)|| (!table.contains("modification"))) {
                                    //System.out.println("NEW TABLE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).setTable(trimDoubleQuotes(table));
                                }
                                else {
                                    //System.out.println("MODIFICATION TABLE");
                                    paragraph.setTable(trimDoubleQuotes(table));
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                }
                                
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Case")) {
                                
                                if (!old.equals(bindingSet.getValue("part").toString())) {
                                    
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
                                    
                                    //System.out.println(case1.getURI());
                                    case1.getPassages().add(passage);
                                    String[] cases = case1.getURI().split("case");
                                    old = bindingSet.getValue("part").toString();
                                    //case1.setText(bindingSet.getValue("text").toString());

                                    if (cases.length > 2) {
                                        //System.out.println("NEW CASE");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                    }
                                    else if (mod==0) {
                                        //System.out.println("NEW CASE");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getCaseList().add(case1);
                                        count4 ++;
                                    }
                                    else if (mod==1) {
                                        //System.out.println("MODIFICATION CASE");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Case");
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(case1);
                                        mod=0;
                                        count4 ++;
                                    }
                                    else {
                                        //System.out.println("PARAGPAPH MODIFICATION CASE");
                                        paragraph.getCaseList().add(case1);
                                        legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                        count4 ++;
                                    }
                                    
                                }
                            
                            }
                            else if ((bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Edit")) || (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Addition")) ||(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Deletion"))) {
                                
                                Modification modification = new Modification();
                                modification.setURI(bindingSet.getValue("part").toString());
                                //System.out.println(modification.getURI());
                                //System.out.println("MODIFICATION");
                                modification.setType(bindingSet.getValue("type").toString());
                                legald.getArticles().get(count).getParagraphs().get(count2).setModification(modification);
                                mod = 1;
                            
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
            System.out.println("1");
            endpointResult.setMessage(e.toString());
            e.printStackTrace();
        }

        // connect to Sesame
        Repository repo = new HTTPRepository(sesameServer, repositoryID);
        try {
            repo.initialize();
        } catch (RepositoryException ex) {
            System.out.println("2");
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

                      System.out.println(endpointResult.getQuery());
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
                System.out.println("3");
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
                    " ?work  dc:title ?title.\n" +
                    " ?work leg:gazette ?gazette.\n" +
                    " ?work dc:created ?date.\n" +
                    " ?gazette dc:title ?gaztitle.\n" ;
                    
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
                                mod.getCompetenceGround().setTitle(trimDoubleQuotes(bindingSet.getValue("title").toString().replace("@el", "")));
                                current = bindingSet.getValue("mod").toString();
                                frag = 0;
                                
                            }

                            if (bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Article")) {
                                
                                Article article = new Article();
                                article.setId(count+2);
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
                                paragraph.setId(count2+2);
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
        
        /*for (Map.Entry entry : params.entrySet()) {
            System.out.println(entry.getKey() + "" + entry.getValue());
        }*/
        
        List<LegalDocument> LDs = new ArrayList<LegalDocument>();
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
                "\n" +
                "SELECT DISTINCT ?legaldocument ?title ?type ?date ?id\n" +
                "WHERE{\n" +
                " ?legaldocument dc:title ?title.\n";
                  
                //search by specific date
                if((params.get("date")==null) || (params.get("date").equals(""))){
                    queryString += "?legaldocument dc:created ?date.\n";
                }
                else if ((params.get("year")==null) || (params.get("year").equals(""))){
                    queryString += "?legaldocument dc:created ?date.\n" + 
                    "FILTER (?date = \"" + params.get("date") + "\"^^xsd:date)\n";
                }
                  
                //search between two dates
                if ((params.get("datefrom")==null) || (params.get("datefrom").equals(""))) {

                }
                else if((params.get("year")==null) || (params.get("year").equals(""))) {
                    queryString += "FILTER ( ?date >= \"" + params.get("datefrom") + "\"^^xsd:date)\n";
                }

                if ((params.get("dateto")==null) || (params.get("dateto").equals(""))) {

                }
                else if ((params.get("year")==null) || (params.get("year").equals(""))){
                    queryString += "FILTER ( ?date <= \"" + params.get("dateto") + "\"^^xsd:date)\n";
                }
                  
                //search by year
                if ((params.get("year")==null) || (params.get("year").equals(""))) {

                }
                else if (((params.get("date")==null) || (params.get("date").equals(""))) && ((params.get("datefrom")==null) || (params.get("datefrom").equals(""))) && ((params.get("dateto")==null) || (params.get("dateto").equals("")))) {
                    queryString += "FILTER ( ?date >= \"" + params.get("year") + "-01-01\"^^xsd:date && ?date <= \"" + params.get("year") + "-12-30\"^^xsd:date)\n";
                }
                  
                //search by fek year, issue and id
                if (((params.get("fek_year")==null) || (params.get("fek_year").equals(""))) && ((params.get("fek_id")==null) || (params.get("fek_id").equals("")))) {

                }
                else {
                    queryString += "?legaldocument leg:gazette ?gazette.\n" +
                    "?gazette dc:title \"" + params.get("fek_issue") + "/" + params.get("fek_year") + "/" + params.get("fek_id") + "\".\n";
                }
                
                //search by legal document id
                if((params.get("id")==null) || (params.get("id").equals(""))){
                    queryString += "?legaldocument leg:legislationID ?id.\n";
                }
                else {
                    queryString += "?legaldocument leg:legislationID \""+ params.get("id") +"\"^^xsd:integer.\n";
                }
                  
                //search by type
                if((params.get("type")==null) || (params.get("type").equals(""))){
                    queryString += " ?legaldocument rdf:type ?type.\n";
                }
                else {
                    String type =  params.get("type");
                    List<String> items = Arrays.asList(type.split("\\s*,\\s*"));
                      
                    if (items.size()>1) {
                          
                        queryString += " ?legaldocument rdf:type ?type.\n";
                        queryString += "FILTER(";
                          
                        for (int i=0;i<items.size();i++) {
                              
                            if (i == items.size()-1) {
                                  
                                if (items.get(i).equals("con")) {
                                    queryString += "?type=leg:Constitution";    
                                }
                                else if (items.get(i).equals("pd")) {
                                    queryString += "?type=leg:PresidentialDecree";
                                }
                                else if (items.get(i).equals("law")) {
                                    queryString += "?type=leg:Law";
                                }
                                else if (items.get(i).equals("amc")) {
                                    queryString += "?type=leg:ActOfMinisterialCabinet";
                                  }
                                else if (items.get(i).equals("md")) {
                                    queryString += "?type=leg:MinisterialDecision";
                                }
                                else if (items.get(i).equals("rd")) {
                                    queryString += "?type=leg:RoyalDecree";
                                }
                                else if (items.get(i).equals("la")) {
                                    queryString += "?type=leg:LegislativeAct";
                                }
                                  
                            }
                            else {
                                  
                                if (items.get(i).equals("con")) {
                                    queryString += "?type=leg:Constitution || ";    
                                }
                                else if (items.get(i).equals("pd")) {
                                    queryString += "?type=leg:PresidentialDecree || ";
                                }
                                else if (items.get(i).equals("law")) {
                                    queryString += "?type=leg:Law || ";
                                }
                                else if (items.get(i).equals("amc")) {
                                    queryString += "?type=leg:ActOfMinisterialCabinet || ";
                                }
                                else if (items.get(i).equals("md")) {
                                    queryString += "?type=leg:MinisterialDecision || ";
                                }
                                else if (items.get(i).equals("rd")) {
                                    queryString += "?type=leg:RoyalDecree || ";
                                }
                                else if (items.get(i).equals("la")) {
                                    queryString += "?type=leg:LegislativeAct || ";
                                }
                                  
                            }
                              
                        }
                          
                        queryString += " )\n";
                          
                    }
                    else {
                          
                        if (items.get(0).equals("con")) {
                            queryString += "?legaldocument rdf:type leg:Constitution.\n";  
                        }
                        else if (items.get(0).equals("pd")) {
                            queryString += "?legaldocument rdf:type leg:PresidentialDecree.\n";  
                        }
                        else if (items.get(0).equals("law")) {
                            queryString += "?legaldocument rdf:type leg:Law.\n";  
                        }
                        else if (items.get(0).equals("amc")) {
                            queryString += "?legaldocument rdf:type leg:ActOfMinisterialCabinet.\n";  
                        }
                        else if (items.get(0).equals("md")) {
                            queryString += "?legaldocument rdf:type leg:MinisterialDecision.\n";  
                        }
                        else if (items.get(0).equals("rd")) {
                            queryString += "?legaldocument rdf:type leg:RoyalDecree.\n";  
                        }
                        else if (items.get(0).equals("la")) {
                            queryString += "?legaldocument rdf:type leg:LegislativeAct.\n";  
                        }
                    
                    }

                }
                
                //search by keyword
                if((params.get("keywords")!=null) && !params.get("keywords").equals("")) {
                    
                    // Tokenize keywords field
                    String[] tokens;
                    if(params.get("keywords").contains(",")){
                        tokens = params.get("keywords").replaceAll(" ", "").split(",");
                    }
                    else{
                        tokens = params.get("keywords").split(" ");
                        for(int i=0; i<tokens.length;i++){
                            tokens[i] = tokens[i].replaceAll(" ", "");
                        }
                    }
                    List<String> stopWords = Arrays.asList("�","�","��","��","��","���","���","���","���","���","���","��","�","�����","�����","�����","�������","�����","���","����","���","����","��" ,"����","���","���","����","��","��","��","����","����","����","����","��","��","��","���","��","���","���","���","���","��","����","���" ,"���" ,"�����" ,"����","����","�����","�����","�����","������","�����","����","����","�����","�����","������","�����","����","�������","������","������","�������","�������","������","�������","��������","����","����","����","���","���");
                    List<String> keywords = new ArrayList<String>();
                    
                    // Stop Words Filtering
                    for (String token : tokens) {
                        if ((!stopWords.contains(token)) || token.equals("")) {
                            keywords.add(token);
                        }
                    }
                    // Stemming
                    for(int i=0; i< keywords.size(); i++){
                        int size;
                        if(keywords.get(i).length() <=5){
                            size = keywords.get(i).length();
                        }
                        else if (keywords.get(i).length() <= 8){
                            size = (int) (keywords.get(i).length() * 0.95);
                        }
                        else{
                            size = (int) (keywords.get(i).length() * 0.8);
                        }
                        keywords.set(i,keywords.get(i).substring(0, size-1));
                        System.out.println(keywords.get(i));
                    }
                    
                    // Add regex filtering
                    queryString += " ?legaldocument metalex:part+ ?part.\n";
                    queryString += " ?part leg:text ?text.\n";
                    queryString += " ?legaldocument leg:tag ?tag.\n";
                    queryString += "FILTER( ";
                    for(String keyword : keywords){
                        queryString += "regex(?text, \"" + keyword +"\") || regex(?title,\"" + keyword +"\") || " +"regex(?tag,\"" + keyword +"\") || ";
                    }
                    queryString += "FALSE )\n";
                }
                  
                queryString += "}";
                
                //System.out.println(queryString);
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                result = tupleQuery.evaluate();

                try {
                    
                    //iterate the result set
                    while (result.hasNext()) {
                        
                        BindingSet bindingSet = result.next();
                        LegalDocument ld = new LegalDocument();
                       
                        if ((params.get("date")==null) || (params.get("date").equals(""))) {
                            String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
                            //String date = bindingSet.getValue("date").toString();
                            date = trimDoubleQuotes(date);
                            ld.setPublicationDate(date);
                            String[] year = date.split("-");
                            ld.setYear(year[0]);
                        }
                        else {
                            String date = params.get("date");
                            ld.setPublicationDate(date);
                            String[] year = date.split("-");
                            ld.setYear(year[0]);
                        }
                        if ((params.get("id")==null) || (params.get("id").equals(""))) {
                            String[] ids = bindingSet.getValue("id").toString().split("^^");
                            String id = ids[0].replace("^^<http://www.w3.org/2001/XMLSchema#integer>","");
                            ld.setId(trimDoubleQuotes(id));
                        }
                        else {
                            String id = params.get("id");
                            ld.setId(trimDoubleQuotes(id));
                        }
                        if ((params.get("type")==null) || (params.get("type").equals(""))) {
                            
                            String type = bindingSet.getValue("type").toString();
                           
                            if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                                ld.setDecisionType("��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                                ld.setDecisionType("��������� �������� (��)");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                                ld.setDecisionType("�����");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                                ld.setDecisionType("(���) ����� ���������� ����������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                                ld.setDecisionType("(��) ��������� �������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                                ld.setDecisionType("(��) �������� ��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                                ld.setDecisionType("(���) ����� ����������� ������������");
                            }
                            
                        }
                        else {
                            
                            String type = params.get("type");
                            
                            if (type.equals("con")) {
                                ld.setDecisionType("��������");
                            }
                            else if (type.equals("pd")) {
                                ld.setDecisionType("��������� �������� (��)");
                            }
                            else if (type.equals("law")) {
                                ld.setDecisionType("�����");
                            }
                            else if (type.equals("amc")) {
                                ld.setDecisionType("(���) ����� ���������� ����������");
                            }
                            else if (type.equals("md")) {
                                ld.setDecisionType("(��) ��������� �������");
                            }
                            else if (type.equals("rd")) {
                                ld.setDecisionType("(��) �������� ��������");
                            }
                            else if (type.equals("la")) {
                                ld.setDecisionType("(���) ����� ����������� ������������");
                            }
                            
                        }
                       
                        String title = bindingSet.getValue("title").toString().replace("@el", "");
                        ld.setTitle(trimDoubleQuotes(title));
                        //String[] URIs = bindingSet.getValue("legaldocument").toString().split("uoa.gr/");
                        ld.setURI(trimDoubleQuotes(bindingSet.getValue("legaldocument").toString()));
                        LDs.add(ld);
                   
                    }
                
                }
                finally {
                    result.close();
                }
            
        }
        catch (OpenRDFException e) {
            // handle exception
        }
        
        return LDs;
        
    }
    
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
                "ORDER BY DESC(?views)\n" +
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
                        String title = bindingSet.getValue("title").toString().replace("@el", "");
                        legald.setTitle(trimDoubleQuotes(title));
                        String id = bindingSet.getValue("id").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                        legald.setId(trimDoubleQuotes(id));
                        String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");  
                        date = trimDoubleQuotes(date);
                        legald.setPublicationDate(date);
                        String[] year = date.split("-");
                        legald.setYear(year[0]);
                        
                        String type = bindingSet.getValue("type").toString();
                           
                            if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                                legald.setDecisionType("��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                                legald.setDecisionType("��������� �������� (��)");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                                legald.setDecisionType("�����");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                                legald.setDecisionType("(���) ����� ���������� ����������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                                legald.setDecisionType("(��) ��������� �������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                                legald.setDecisionType("(��) �������� ��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                                legald.setDecisionType("(���) ����� ����������� ������������");
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
                        String title = bindingSet.getValue("title").toString().replace("@el", "");
                        legald.setTitle(trimDoubleQuotes(title));
                        String id = bindingSet.getValue("id").toString().replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                        legald.setId(trimDoubleQuotes(id));
                        String date = bindingSet.getValue("date").toString().replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");  
                        date = trimDoubleQuotes(date);
                        legald.setPublicationDate(date);
                        String[] year = date.split("-");
                        legald.setYear(year[0]);
                        
                        String type = bindingSet.getValue("type").toString();
                           
                            if (type.equals("http://legislation.di.uoa.gr/ontology/Constitution")) {
                                legald.setDecisionType("��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")) {
                                legald.setDecisionType("��������� �������� (��)");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/Law")) {
                                legald.setDecisionType("�����");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")) {
                                legald.setDecisionType("(���) ����� ���������� ����������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")) {
                                legald.setDecisionType("(��) ��������� �������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/RoyalDecree")) {
                                legald.setDecisionType("(��) �������� ��������");
                            }
                            else if (type.equals("http://legislation.di.uoa.gr/ontology/LegislativeAct")) {
                                legald.setDecisionType("(���) ����� ����������� ������������");
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
    
    public static String trimDoubleQuotes(String text) {
        
        int textLength = text.length();
        
        if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
            return text.substring(1, textLength - 1);
        }
        
        return text;
    
    }
    
    // HTTP GET request
    private String getKML(String place) {

            EndpointResult result = null;
            Properties props = new Properties();
            InputStream fis = null;
            String host = "";
            int port = 0;
            String appName = "";
            try {

                fis = getClass().getResourceAsStream("/properties.properties");
                props.load(fis);

                // get the properties values
                host = props.getProperty("StrabonHost");
                port = Integer.parseInt(props.getProperty("StrabonPort"));
                appName = props.getProperty("StrabonAppName");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String KML = "";
            String query = "SELECT ?geo WHERE {<"+place+"> ?hasgeometry ?geo. ?hasgeometry <http://www.w3.org/2000/01/rdf-schema#label> \"has_geometry\"@en.}"; 

            SPARQLEndpoint endpoint = new SPARQLEndpoint(host, port, appName);

            try {
                
                result = endpoint.query(query, stSPARQLQueryResultFormat.KML);
                
                System.out.println("Status code: " + result.getStatusCode());
                System.out.println("Query: " + query);
                System.out.println("Status text: " + result.getStatusText());
                KML = result.getResponse().replace("<?xml version='1.0' encoding='UTF-8'?>", "");
                KML = KML.replaceAll("\n", "");
                KML = KML.replaceAll("\r", "");
                System.out.println("<----- Result ----->");
                System.out.println(KML);
                System.out.println("<----- Result ----->");

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return KML;

    }
    
}
