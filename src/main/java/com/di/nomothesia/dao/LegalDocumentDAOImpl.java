/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.dao;

import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.EndpointResult;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.model.Paragraph;
import com.di.nomothesia.model.Passage;
import com.di.nomothesia.model.Signer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

/**
 *
 * @author Ilias
 */
public class LegalDocumentDAOImpl implements LegalDocumentDAO{

    @Override
    public LegalDocument getMetadataById(String decisionType, String year, String id) {
        
        LegalDocument legald = new LegalDocument();
        legald.setId(id);
        legald.setYear(year);
        if(decisionType.equals("pd")){
            legald.setDecisionType("(пд) пяоедяийо диатацла");
        }
        else if(decisionType.equals("law")){
            legald.setDecisionType("молос");
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

        // connect to Sesame
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
                    "SELECT ?title ?date ?gaztitle ?signername ?signertitle\n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " dc:title ?title.\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " dc:created ?date.\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " leg:gazette ?gazette.\n" +
                    " ?gazette dc:title ?gaztitle.\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " dc:created ?date.\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " leg:signer ?signer.\n" +
                    " ?signer foaf:name ?signername.\n" +
                    " ?signer foaf:title ?signertitle.\n" +
                    "}";
                  
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                  try {
                    //iterate the result set
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next(); 
                            Signer sign = new Signer();
                            legald.setTitle(trimDoubleQuotes(bindingSet.getValue("title").toString()));
                            legald.setPublicationDate(trimDoubleQuotes(bindingSet.getValue("date").toString()));
                            legald.setFEK(trimDoubleQuotes(bindingSet.getValue("gaztitle").toString()));
                            sign.setFullName(trimDoubleQuotes(bindingSet.getValue("signername").toString()));
                            sign.setTitle(trimDoubleQuotes(bindingSet.getValue("signertitle").toString()));
                            legald.getSigners().add(sign);
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
       
        System.out.println(legald.getFEK());
        for(int i=0; i < legald.getSigners().size(); i++){
            System.out.println(legald.getSigners().get(i).getFullName() + " - " +legald.getSigners().get(i).getTitle());
        }
        return legald;

    }
    
    @Override
    public LegalDocument getById(String decisionType, String year, String id) {
        
        LegalDocument legald = this.getMetadataById(decisionType, year, id);
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

        // connect to Sesame
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
                    "SELECT ?part ?text ?type ?title\n" +
                    "WHERE{\n" +
                    "  <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:part+  ?part.\n" +
                    " ?part rdf:type ?type.\n" +
                    "OPTIONAL{ ?part leg:text ?text.}.\n" +
                    "OPTIONAL{ ?part dc:title ?title.}.\n" +
                    "}" +
                    "ORDER BY ?part";
                  
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                  try {
                    //iterate the result set
                    int count = -1;
                    int count2 = -1;
                    int count3 = -1;
                    int count4 = -1;
                    int mod = 0;
                    Paragraph paragraph = null;
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next(); 
                            if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Article")){
                                Article article = new Article();
                                article.setId(count+2);
                                article.setURI(bindingSet.getValue("part").toString());
                                if(bindingSet.getValue("title")!=null){
                                    String title = bindingSet.getValue("title").toString();
                                    article.setTitle(trimDoubleQuotes(title));
                                }
                                System.out.println(article.getURI());
                                System.out.println("NEW ARTICLE");
                                legald.getArticles().add(article);
                                count ++;
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                mod = 0;
                            }
                            else if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")){
                                paragraph = new Paragraph();
                                paragraph.setId(count2+2);
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                System.out.println(paragraph.getURI());
                                if((mod==0)||(mod==2)){
                                    System.out.println("NEW PARAGRAPH");
                                    legald.getArticles().get(count).getParagraphs().add(paragraph);
                                    count2++;
                                    count3 = -1;
                                    count4 = -1;
                                    mod = 0;
                                }
                                else{
                                    System.out.println("MODIFICATION PARAGRAPH");
                                    mod = 2;
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Paragraph");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                }
                            }
                            else if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")){
                                Passage passage = new Passage();
                                passage.setId(count3+2);
                                passage.setURI(bindingSet.getValue("part").toString());
                                String text = bindingSet.getValue("text").toString();
                                passage.setText(trimDoubleQuotes(text));
                                System.out.println(passage.getURI());
                                if((mod==0)|| (!passage.getURI().contains("modification"))){
                                    System.out.println("NEW PASSAGE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getPassages().add(passage);
                                }
                                else if(mod==1){
                                     System.out.println("MODIFICATION PASSAGE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Passage");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(passage);
                                    mod =0;
                                }
                                else{
                                    System.out.println("PARAGRAPH MODIFICATION PASSAGE");
                                    paragraph.getPassages().add(passage);
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                }
                                count3 ++;
                            }
                            else if (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Table")){
                                String table = bindingSet.getValue("text").toString();
                                System.out.println(bindingSet.getValue("part").toString());
                                if((mod==0)|| (!table.contains("modification"))){
                                    System.out.println("NEW TABLE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).setTable(trimDoubleQuotes(table));
                                }
                                else{
                                    System.out.println("MODIFICATION TABLE");
                                    paragraph.setTable(table);
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                }
                            }
                            else if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Case")){
                                Case case1 = new Case();
                                case1.setId(count4+2);
                                case1.setURI(bindingSet.getValue("part").toString());
                                Passage passage = new Passage();
                                String text = bindingSet.getValue("text").toString();
                                passage.setText(trimDoubleQuotes(text));
                                System.out.println(case1.getURI());
                                case1.getPassages().add(passage);
                                String[] cases = case1.getURI().split("case");
                                //case1.setText(bindingSet.getValue("text").toString());
                                if (cases.length > 2){
                                    System.out.println("NEW CASE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getCaseList().get(count4).getCaseList().add(case1);
                                }
                                else if(mod==0){
                                    System.out.println("NEW CASE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getCaseList().add(case1);
                                    count4 ++;
                                }
                                else if(mod==1){
                                    System.out.println("MODIFICATION CASE");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setType("Case");
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(case1);
                                    mod=0;
                                    count4 ++;
                                }
                                else{
                                    System.out.println("PARAGPAPH MODIFICATION CASE");
                                    paragraph.getCaseList().add(case1);
                                    legald.getArticles().get(count).getParagraphs().get(count2).getModification().setFragment(paragraph);
                                    count4 ++;
                                }
                            }
                            else if((bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Edit")) || (bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Creation")) ||(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Deletion"))){
                                Modification modification = new Modification();
                                modification.setURI(bindingSet.getValue("part").toString());
                                System.out.println(modification.getURI());
                                System.out.println("MODIFICATION");
                                modification.setType(bindingSet.getValue("type").toString());
                                legald.getArticles().get(count).getParagraphs().get(count2).setModification(modification);
                                mod = 1;
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
        
         System.out.println("=========================================================================================");
         for (int i = 0; i<legald.getArticles().size(); i++) {
            for (int j = 0; j<legald.getArticles().get(i).getParagraphs().size(); j++) {

                String paragraph = "\n" + legald.getArticles().get(i).getParagraphs().get(j).getId();
                for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
                    paragraph += legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                }

                for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
                    paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();
                    for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
                        paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                    }
                    paragraph +="\n";
                }
                
                if(legald.getArticles().get(i).getParagraphs().get(j).getModification() != null){
                    if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Paragraph")){
                        Paragraph p = (Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                        paragraph += "\n\"";
                        for (int k = 0; k<p.getPassages().size(); k++) {
                            paragraph += p.getPassages().get(k).getText();
                        }

                        for (int k = 0; k< p.getCaseList().size(); k++) {
                            paragraph += p.getCaseList().get(k).getId();
                            for (int l = 0; l<p.getCaseList().get(k).getPassages().size(); l++) {
                                paragraph += p.getCaseList().get(k).getPassages().get(l).getText();
                            }
                        }
                        paragraph +="\n";
                    }
                    else if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Case")){
                        Case c = (Case) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                        for (int l = 0; l<c.getPassages().size(); l++) {
                            paragraph += c.getPassages().get(l).getText();
                        }
                    }
                    paragraph += "\"\n";
                }
                System.out.println(paragraph);
            }

        }
        System.out.println("=========================================================================================");

         
        return legald;

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

        // connect to Sesame
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
                  
                  System.out.println(queryString);
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
                  finally{
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
    public EndpointResult sparqlQuery(EndpointResult endpointResult) {
        
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

        endpointResult.setResults(results);
        
        return endpointResult;

    }

    @Override
    public List<Modification> getModifications(String decisionType, String year, String id, String date) {
        
        
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

        // connect to Sesame
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
                    "SELECT ?mod ?type ?patient ?part ?type2 ?text\n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:realizedBy  ?version.\n" +
                    " ?version metalex:matterOf ?mod.\n" +
                    " ?mod rdf:type ?type.\n" +
                    " ?mod metalex:patient ?patient.\n" +
                    " ?mod metalex:part+ ?part.\n" +
                    " ?part rdf:type ?type2.\n" +
                    " ?mod  metalex:legislativeCompetenceGround ?work.\n" ;
                  
                  if(date !=null){
                    queryString += " ?work dc:created ?date.\n" +
                    "OPTIONAL{\n" +
                    " ?part leg:text ?text.\n" +
                    "}}\n" +
                    "ORDER BY ?mod";
                  }
                  else{
                    queryString += " ?work dc:created ?date.\n" +
                    "OPTIONAL{\n" +
                    " ?part leg:text ?text.\n" +
                    "}}\n" +
                    "ORDER BY ?mod "; 
                  }
                  
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                  try {
                    //iterate the result set
                    int counter = -1;
                    int count = -1;
                    int count2 = -1;
                    int count3 = -1;
                    int count4 = -1;
                    int frag = 0;
                    Fragment fragment = null;
                    String current = "";
                    Modification mod = null;
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next();
                            if (!bindingSet.getValue("mod").toString().equals(current)){
                                if(mod != null){
                                  mod.setFragment(fragment);
                                  modifications.add(mod);
                                }
                                mod = new Modification();
                                mod.setURI(bindingSet.getValue("mod").toString());
                                mod.setType(bindingSet.getValue("type").toString());
                                mod.setPatient(bindingSet.getValue("patient").toString());
                                current = bindingSet.getValue("mod").toString();
                                frag = 0;
                            }
                            
                            if(bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Article")){
                                Article article = new Article();
                                article.setId(count+2);
                                article.setURI(bindingSet.getValue("part").toString());
                                System.out.println(article.getURI());
                                System.out.println("NEW ARTICLE");
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                                fragment = article;
                                frag = 1;
                            }
                            else if(bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")){
                                Paragraph paragraph = new Paragraph();
                                paragraph.setId(count2+2);
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                System.out.println(paragraph.getURI());
                                System.out.println("NEW PARAGRAPH");
                                if(frag == 0){
                                    fragment = paragraph;
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
                            else if(bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")){
                                Passage passage = new Passage();
                                passage.setId(count3+2);
                                passage.setURI(bindingSet.getValue("part").toString());
                                String text = bindingSet.getValue("text").toString();
                                passage.setText(trimDoubleQuotes(text));
                                System.out.println(passage.getURI());
                                System.out.println("NEW PASSAGE");
                                if(frag == 0){
                                    fragment = passage;
                                }
                                else if (frag == 1){
                                    Article article = (Article) fragment;
                                    article.getParagraphs().get(count2).getPassages().add(passage);
                                    fragment = article;
                                }
                                else{
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getPassages().add(passage);
                                    fragment = paragraph;
                                }
                                count3 ++;
                            }
                            else if(bindingSet.getValue("type2").toString().equals("http://legislation.di.uoa.gr/ontology/Case")){
                                Case case1 = new Case();
                                case1.setId(count4+2);
                                case1.setURI(bindingSet.getValue("part").toString());
                                Passage passage = new Passage();
                                String text = bindingSet.getValue("text").toString();
                                passage.setText(trimDoubleQuotes(text));
                                System.out.println(case1.getURI());
                                case1.getPassages().add(passage);
                                System.out.println("NEW CASE");
                                if(frag == 0){
                                    fragment = case1;
                                }
                                else if (frag == 1){
                                    Article article = (Article) fragment;
                                    article.getParagraphs().get(count2).getCaseList().add(case1);
                                    fragment = article;
                                }
                                else{
                                    Paragraph paragraph = (Paragraph) fragment;
                                    paragraph.getCaseList().add(case1);
                                    fragment = paragraph;
                                }
                                count4 ++;
                            }

                   }
                   if(mod != null){
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
        
        for(int i =0; i< modifications.size(); i++){
            System.out.println(modifications.get(i).getURI());
            System.out.println(modifications.get(i).getType());
            System.out.println(modifications.get(i).getPatient());
//            if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Paragraph")){
//                Paragraph p = (Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
//                paragraph += "\n\"";
//                for (int k = 0; k<p.getPassages().size(); k++) {
//                    paragraph += p.getPassages().get(k).getText();
//                }
//
//                for (int k = 0; k< p.getCaseList().size(); k++) {
//                    paragraph += p.getCaseList().get(k).getId();
//                    for (int l = 0; l<p.getCaseList().get(k).getPassages().size(); l++) {
//                        paragraph += p.getCaseList().get(k).getPassages().get(l).getText();
//                    }
//                }
//                paragraph +="\n";
//            }
//            else if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Case")){
//                Case c = (Case) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
//                for (int l = 0; l<c.getPassages().size(); l++) {
//                    paragraph += c.getPassages().get(l).getText();
//                }
//            }
        }
         
        return modifications;
    }
    
    @Override
    public List<LegalDocument> getAllModifications(String decisionType, String year, String id) {
        
        List<LegalDocument> legalds = new ArrayList<LegalDocument>();
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

        // connect to Sesame
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
                    "SELECT ?work ?title ?date ?gaztitle\n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:realizedBy  ?version.\n" +
                    " ?version metalex:matterOf ?mod.\n" +
                    " ?mod  metalex:legislativeCompetenceGround ?work.\n" +
                    " ?work dc:title ?title.\n" +
                    " ?work dc:created ?date.\n" +
                    " ?work leg:gazette ?gazette.\n" +
                    " ?gazette dc:title ?gaztitle.\n" +
                    "}" + 
                    "GROUP BY ?work ?title ?date ?gaztitle\n" +
                    "ORDER BY ?date\n";

                  
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                  try {
                    //iterate the result set
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next();
                            LegalDocument legald = new LegalDocument();
                            if(bindingSet.getValue("work")!=null){
                                legald.setURI(bindingSet.getValue("work").toString());
                                legald.setTitle(trimDoubleQuotes(bindingSet.getValue("title").toString()));
                                legald.setPublicationDate(trimDoubleQuotes(bindingSet.getValue("date").toString()));
                                legald.setFEK(trimDoubleQuotes(bindingSet.getValue("gaztitle").toString()));
                                legalds.add(legald);
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
        
        
        return legalds;
    }
    
    public static String trimDoubleQuotes(String text) {
        int textLength = text.length();
        if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
            return text.substring(1, textLength - 1);
        }
        return text;
    }

    @Override
    public List<LegalDocument> search(Map<String, String> params) {
        
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

        // connect to Sesame
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
                    "SELECT ?title ?type ?date ?id\n" +
                    "WHERE{\n" +
                    " ?legaldocument dc:title ?title.\n";
                  
                  if((params.get("date")==null) || (params.get("date").equals(""))){
                      queryString += "?legaldocument dc:created ?date.\n";
                  }
                  else{
                      queryString += "?legaldocument dc:created "+ params.get("date") +".\n";
                  }
                  
                  if((params.get("id")==null) || (params.get("id").equals(""))){
                      queryString += "?legaldocument leg:legislationID ?id.\n";
                  }
                  else{
                      queryString += "?legaldocument leg:legislationID \""+ params.get("id") +"\".\n";
                  }
                  
                  if((params.get("type")==null) || (params.get("type").equals(""))){
                      queryString += " ?legaldocument rdf:type ?type.\n";
                  }
                  else{
                      String type =  params.get("type");
                      if(type.equals("con")){
                          queryString += " ?legaldocument rdf:type leg:Constitution.\n";
                      }
                      else if(type.equals("pd")){
                          queryString += " ?legaldocument rdf:type leg:PresidentialDecree.\n";
                      }
                      else if(type.equals("law")){
                          queryString += " ?legaldocument rdf:type leg:Law.\n";
                      }
                      else if(type.equals("amc")){
                          queryString += " ?legaldocument rdf:type leg:ActOfMinisterialCabinet.\n";
                      }
                      else if(type.equals("md")){
                          queryString += " ?legaldocument rdf:type leg:MinisterialDecision.\n";
                      }
                  }
                  
                  queryString += "}";
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                try {
                    //iterate the result set
                   while (result.hasNext()) {
                       BindingSet bindingSet = result.next();
                       LegalDocument ld = new LegalDocument();
                       if((params.get("date")==null) || (params.get("date").equals(""))){
                           String date = bindingSet.getValue("date").toString();
                           date = trimDoubleQuotes(date);
                           ld.setPublicationDate(date);
                           String[] year = date.split("-");
                           ld.setYear(year[0]);
                       }
                       else{
                           String date = params.get("date");
                           ld.setPublicationDate(date);
                           String[] year = date.split("-");
                           ld.setYear(year[0]);
                       }
                       if((params.get("id")==null) || (params.get("id").equals(""))){
                           String[] ids = bindingSet.getValue("id").toString().split("^^");
                           String id = ids[0].replace("^^","");
                           id = trimDoubleQuotes(id);
                           ld.setId(id);
                       }
                       else{
                           String id = params.get("id");
                           ld.setId(trimDoubleQuotes(id));
                       }
                       if((params.get("type")==null) || (params.get("type").equals(""))){
                           String type = bindingSet.getValue("type").toString();
                           if(type.equals("http://legislation.di.uoa.gr/ontology/Constitution")){
                               ld.setDecisionType("сумтацла");
                           }
                           else if(type.equals("http://legislation.di.uoa.gr/ontology/PresidentialDecree")){
                               ld.setDecisionType("пяоедяийо диатацла (пд)");
                           }
                           else if(type.equals("http://legislation.di.uoa.gr/ontology/Law")){
                               ld.setDecisionType("молос");
                           }
                           else if(type.equals("http://legislation.di.uoa.gr/ontology/ActOfMinisterialCabinet")){
                               ld.setDecisionType("(пус) пяанг упоуяцийоу сулбоукиоу");
                           }
                           else if(type.equals("http://legislation.di.uoa.gr/ontology/MinisterialDecision")){
                               ld.setDecisionType("(уа) упоуяцийг апожасг");
                           }
                       }
                       else{
                           String type = params.get("type");
                           if(type.equals("con")){
                               ld.setDecisionType("сумтацла");
                           }
                           else if(type.equals("pd")){
                               ld.setDecisionType("пяоедяийо диатацла (пд)");
                           }
                           else if(type.equals("law")){
                               ld.setDecisionType("молос");
                           }
                           else if(type.equals("amc")){
                               ld.setDecisionType("(пус) пяанг упоуяцийоу сулбоукиоу");
                           }
                           else if(type.equals("md")){
                               ld.setDecisionType("(уа) упоуяцийг апожасг");
                           }
                       }
                       
                       String title = bindingSet.getValue("title").toString();
                       ld.setTitle(trimDoubleQuotes(title));
                       LDs.add(ld);
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
        
        return LDs;
    }
}
