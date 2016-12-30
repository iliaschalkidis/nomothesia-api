package com.di.nomothesia.service;

import com.di.nomothesia.controller.XMLBuilder;
import com.di.nomothesia.controller.XMLBuilder2;
import com.di.nomothesia.dao.LegalDocumentDAO;
import com.di.nomothesia.dao.LegalDocumentDAOImpl;
import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class LegislationServiceImpl  implements LegislationService {
    
    @Override
    public LegalDocument getById(String decisionType, String year, String id, int request) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Metadata
        LegalDocument legald = legalDocumentDAO.getMetadataById(decisionType, year, id);
        
        //Get Citations
        legald = legalDocumentDAO.getCitationsById(decisionType, year, id, request, legald);
        
        //Get Legal Document
        return legalDocumentDAO.getById(decisionType, year, id, request, legald);
    }
    
    @Override
    public List<Modification> getAllModificationsById(String type, String year, String id,int request, String date) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getAllModifications(type, year, id, date, request);
        
        return mods;
    }
    
    @Override
    @Cacheable (value="cacheManager", key="#query")
    public EndpointResultSet sparqlQuery(String query, String format) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Set query
        EndpointResultSet eprs = new EndpointResultSet();
        if("1".equals(query)) {
            eprs.setQuery(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX nomothesia: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX eli: <http://data.europa.eu/eli/ontology#>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT ?doc (COUNT(DISTINCT ?expression) AS ?versions)\n" +
                      "WHERE{\n" +
                      "?doc eli:is_realized_by ?expression.\n" +
                      "?expression metalex:matterOf ?modification.\n" +
                      "?modification metalex:legislativeCompetenceGround\n" +
                      "?doc2.\n" +
                      "?doc2 eli:date_publication ?date.\n" +
                      "FILTER (?date >= \"2008-01-01\"^^xsd:date\n" +
                      "&& ?date <= \"2013-01-31\"^^xsd:date)\n" +
                      "} GROUP BY ?doc ?title\n" +
                      "ORDER BY DESC(?versions) LIMIT 5"
            );
        } else if("2".equals(query)) {
            eprs.setQuery(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX nomothesia: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX eli: <http://data.europa.eu/eli/ontology#>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT ?article (COUNT(?passage) as ?passages)\n" +
                      "WHERE{\n" +
                      "?doc eli:has_part+ ?article.\n" +
                      "?doc eli:date_publication ?date.\n" +
                      "?article rdf:type nomothesia:Article.\n" +
                      "?article eli:has_part+ ?passage.\n" +
                      "?passage rdf:type nomothesia:Passage.\n" +
                      "FILTER (?date >= \"2015-01-01\"^^xsd:date\n" +
                      "&& ?date <= \"2015-12-31\"^^xsd:date)\n" +
                      "} GROUP BY ?article\n" +
                      "ORDER BY DESC(?passages) LIMIT 1"
            );
        } else if("3".equals(query)) {
            eprs.setQuery(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX metalex:<http://www.metalex.eu/metalex/2008-05-02#>\n" +
                    "PREFIX nomothesia: <http://legislation.di.uoa.gr/ontology/>\n" +
                    "PREFIX eli: <http://data.europa.eu/eli/ontology#>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "\n" +
                    "SELECT ?signatory_name (COUNT(?doc) AS ?docs)\n" +
                      "WHERE{\n" +
                      "?doc eli:passed_by ?signatory.\n" +
                      "?doc eli:date_publication ?date.\n" +
                      "?signatory foaf:name ?signatory_name.\n" +
                      "FILTER ( ?date >= \"2008-01-01\"^^xsd:date\n" +
                      "&& ?date <= \"2015-12-31\"^^xsd:date)\n" +
                      "} GROUP BY ?signatory_name\n" +
                      "ORDER BY DESC(?docs) LIMIT 4"
            );
        } else{
            eprs.setQuery(query);
        }
        
        //Get Query Result
        return legalDocumentDAO.sparqlQuery(eprs,format);
    }
    
    @Override
    public String getRDFById(String type, String year, String id) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get RDF Metadata
        return legalDocumentDAO.getRDFById(type, year, id);
    }
    
    @Override
    public String getXMLById(String type, String year, String id, int request) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Metadata
        LegalDocument legald = legalDocumentDAO.getMetadataById(type, year, id);
        
        //Get Citations
        legald = legalDocumentDAO.getCitationsById(type, year, id, request, legald);
        
        //Get Legal Document
        legald = legalDocumentDAO.getById(type, year, id, request, legald);
        
        // Build XML
        XMLBuilder xmlbuild = new XMLBuilder();
        return  xmlbuild.XMLbuilder(legald);
    }
    
    @Override
    public List<LegalDocument> searchLegislation(Map<String, String> params) {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.search(params);
    }
    
    @Override
    public List<LegalDocument> mostViewed() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getViewed();
    }
    
    @Override
    public List<LegalDocument> mostRecent() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getRecent();
    }
    
    @Override
    public List<String> getTags(){
          //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        return legalDocumentDAO.getTags();
    }
    
    @Override
    public List<Fragment> getUpdatedById(LegalDocument legald, List<Modification> mods) {
         //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);

        //Apply Modifications
       return legald.applyModifications(mods);

    }
    
    @Override
    public String getUpdatedXMLById(String type, String year, String id, int request) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
        
        //Get Metadata
        LegalDocument legald = legalDocumentDAO.getMetadataById(type, year, id);
        
        //Get Citations
        legald = legalDocumentDAO.getCitationsById(type, year, id, request, legald);
        
        //Get Legal Document
        legald = legalDocumentDAO.getById(type, year, id, request, legald);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getAllModifications(type, year, id, null, request);
        
        //Apply Modifications
        legald.applyModifications(mods);
        
        // Build XML
        if(!legald.getChapters().isEmpty()){
            XMLBuilder2 xmlbuild = new XMLBuilder2();
            return  xmlbuild.XMLbuilder2(legald);
        }
        else{
            XMLBuilder xmlbuild = new XMLBuilder();
            return  xmlbuild.XMLbuilder(legald);        
        }
    }
    
    @Override
    public String getUpdatedXMLByIdDate(String type, String year, String id, int request, String date) throws TransformerException {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
       
        //Get Metadata
        LegalDocument legald = legalDocumentDAO.getMetadataById(type, year, id);
        
        //Get Citations
        legald = legalDocumentDAO.getCitationsById(type, year, id, request, legald);
        
        //Get Legal Document
        legald = legalDocumentDAO.getById(type, year, id, request, legald);
        
        //Get all Modifications
        List<Modification> mods = legalDocumentDAO.getAllModifications(type, year, id, date, request);
        
        //Apply Modifications
        legald.applyModifications(mods);
        
        // Build XML
        if(!legald.getChapters().isEmpty()){
            XMLBuilder2 xmlbuild = new XMLBuilder2();
            return  xmlbuild.XMLbuilder2(legald);
        }
        else{
            XMLBuilder xmlbuild = new XMLBuilder();
            return  xmlbuild.XMLbuilder(legald);        
        }
    }
    
    @Override
    public List<GovernmentGazette> getFEKStatistics() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
       
        //Get gazettes
        return legalDocumentDAO.getFEKStatistics();
    }
    
    @Override
    public List<ArrayList<String>> getStats() {
        //Get the Spring Context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
         
        //Get the ProductDAO Bean
        LegalDocumentDAO legalDocumentDAO = ctx.getBean("legalDocumentDAO", LegalDocumentDAO.class);
       
        //Get gazettes
        return legalDocumentDAO.getStatistics();
    }
    
}
