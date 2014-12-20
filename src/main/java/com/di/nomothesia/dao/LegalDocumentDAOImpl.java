/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.dao;

import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.model.Paragraph;
import com.di.nomothesia.model.Passage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
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

/**
 *
 * @author Ilias
 */
public class LegalDocumentDAOImpl implements LegalDocumentDAO{

    @Override
    public LegalDocument getMetadataById(String decisionType, String year, String id) {
        
        LegalDocument legald = new LegalDocument();
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
                    "SELECT ?title ?date\n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " dc:title ?title.\n" +
                    " <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " dc:created ?date.\n" +
                    "}";
                  
                  System.out.println(queryString);
                  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                  result = tupleQuery.evaluate();

                  try {
                    //iterate the result set
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next(); 
                            legald.setTitle(trimDoubleQuotes(bindingSet.getValue("title").toString()));
                            legald.setPublicationDate(trimDoubleQuotes(bindingSet.getValue("date").toString()));
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
                    "SELECT ?part ?text ?type\n" +
                    "WHERE{\n" +
                    "  <http://legislation.di.uoa.gr/" + decisionType + "/" + year + "/" + id +">" +
                    " metalex:part+  ?part.\n" +
                    " ?part rdf:type ?type.\n" +
                    "OPTIONAL{\n" +
                    " ?part leg:text ?text.\n" +
                    "}\n" +
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
                    while (result.hasNext()) {
                            BindingSet bindingSet = result.next(); 
                            if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Article")){
                                Article article = new Article();
                                article.setId(count+2);
                                article.setURI(bindingSet.getValue("part").toString());
                                System.out.println(article.getURI());
                                legald.getArticles().add(article);
                                count ++;
                                count2 = -1;
                                count3 = -1;
                                count4 = -1;
                            }
                            else if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Paragraph")){
                                Paragraph paragraph = new Paragraph();
                                paragraph.setId(count2+2);
                                paragraph.setURI(bindingSet.getValue("part").toString());
                                System.out.println(paragraph.getURI());
                                legald.getArticles().get(count).getParagraphs().add(paragraph);
                                count2++;
                            }
                            else if(bindingSet.getValue("type").toString().equals("http://legislation.di.uoa.gr/ontology/Passage")){
                                Passage passage = new Passage();
                                passage.setId(count3+2);
                                passage.setURI(bindingSet.getValue("part").toString());
                                String text = bindingSet.getValue("text").toString();
                                passage.setText(trimDoubleQuotes(text));
                                System.out.println(passage.getURI());
                                legald.getArticles().get(count).getParagraphs().get(count2).getPassages().add(passage);
                                count3 ++;
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
                                //case1.setText(bindingSet.getValue("text").toString());
                                legald.getArticles().get(count).getParagraphs().get(count2).getCaseList().add(case1);
                                count4 ++;
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
        
         for (int i = 0; i<legald.getArticles().size(); i++) {
            for (int j = 0; j<legald.getArticles().get(i).getParagraphs().size(); j++) {

                String paragraph = "" + legald.getArticles().get(i).getParagraphs().get(j).getId();
                for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
                    paragraph += legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                }

                for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
                    paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();
                    for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
                        paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                    }
                }
                System.out.println(paragraph);
            }

        }
         
        return legald;

    }

    @Override
    public List<Modification> getAllModifications(String decisionType, String year, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static String trimDoubleQuotes(String text) {
        int textLength = text.length();
        if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
            return text.substring(1, textLength - 1);
        }
        return text;
    }
}
