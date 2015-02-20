package com.di.nomothesia.service;

import com.di.nomothesia.controller.XMLBuilder;
import com.di.nomothesia.dao.LegalDocumentDAO;
import com.di.nomothesia.model.EndpointResult;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LegislationService {
    
    public LegalDocument getById(String decisionType, String year, String id, int request) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        return legalDocumentDAO.getById(decisionType, year, id, request);
    }
    
    public List<LegalDocument> getAllModificationsById(String decisionType, String year, String id) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        return legalDocumentDAO.getAllModifications(decisionType, year, id);
    }

    public EndpointResult sparqlQuery(String query) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Set query
        EndpointResult eprs = new EndpointResult();
        if(query.equals("1")) {
            eprs.setQuery("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT ?modification ?type ?version ?competenceground \n" +
                    "WHERE{\n" +
                    "  ?version metalex:matterOf ?modification.\n" +
                    "  ?modification metalex:legislativeCompetenceGround ?competenceground.\n" + 
                    "  ?modification rdf:type ?type.\n" + 
                    "  ?version metalex:realizes <http://legislation.di.uoa.gr/pd/2011/54>.\n"
                    + "}"
                    
            );
        }
        else if(query.equals("2")) {
            eprs.setQuery("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT ?part ?type \n" +
                    "WHERE{\n" +
                    " <http://legislation.di.uoa.gr/pd/2014/165> metalex:part+  ?part.\n" +
                    " ?part rdf:type ?type.\n" +
                    "}" +
                    "ORDER BY ?part" 
            );
            
        }
        else if(query.equals("3")) {
            eprs.setQuery("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX leg: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "\n" +
                    "SELECT ?signer ?name (COUNT(?decision) AS ?decisions)\n" +
                    "WHERE{\n" +
                    " ?decision leg:signer  ?signer.\n" +
                    " ?signer foaf:name ?name.\n" +
                    "}" +
                    "GROUP BY ?signer ?name\n" +
                    "ORDER BY DESC(?decisions)"
            );
        }
        else{
            eprs.setQuery(query);
        }
        
        //Get Query Result
        return legalDocumentDAO.sparqlQuery(eprs);
    }

    public String getRDFById(String type, String year, String id) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get RDF Metadata
        return legalDocumentDAO.getRDFById(type, year, id);
    }

    public String getXMLById(String type, String year, String id, int request) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        LegalDocument legald = legalDocumentDAO.getById(type, year, id, request);
        
        // Build XML
        XMLBuilder xmlbuild = new XMLBuilder();
        return  xmlbuild.XMLbuilder(legald);
    }

    public List<LegalDocument> searchLegislation(Map<String, String> params) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.search(params);
    }
    
    public List<LegalDocument> MostViewed() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getViewed();
    }
    
    public List<LegalDocument> MostRecent() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getRecent();
    }
    
    public List<String> getTags(){
          //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getTags();
    }

    public LegalDocument getUpdatedById(String type, String year, String id, int request, String date) {
         //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        LegalDocument legald = legalDocumentDAO.getById(type, year, id, request);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getModifications(type, year, id, date, request);
        
        //Apply Modifications
        legald.applyModifications(mods);
        
        return legald;
    }

    public String getUpdatedXMLById(String type, String year, String id, int request) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        LegalDocument legald = legalDocumentDAO.getById(type, year, id, request);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getModifications(type, year, id, null, request);
        
        //Apply Modifications
        legald.applyModifications(mods);
        
        // Build XML
        XMLBuilder xmlbuild = new XMLBuilder();
        return  xmlbuild.XMLbuilder(legald);
    }
    
    public String getUpdatedXMLByIdDate(String type, String year, String id, int request, String date) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Legal Document
        LegalDocument legald = legalDocumentDAO.getById(type, year, id, request);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getModifications(type, year, id, date, request);
        
        //Apply Modifications
        legald.applyModifications(mods);
        
        // Build XML
        XMLBuilder xmlbuild = new XMLBuilder();
        return  xmlbuild.XMLbuilder(legald);
    }
    
}
