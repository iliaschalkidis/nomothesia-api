package com.di.nomothesia.dao;

import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface LegalDocumentDAO {
    
    /**
     * Gets Metadata for the given legal document (tags, signers etc.).
     *
     * @param decisionType the decision type
     * @param year the year
     * @param id the id
     * @return LegalDocument
     */
    LegalDocument getMetadataById(String decisionType, String year, String id);
    
    /**
     * Gets Citations of a legal document given its Id.
     *
     * @param decisionType the decision type
     * @param year the year
     * @param id the id
     * @param req the request
     * @param legald the legal document
     * @return LegalDocument
     */
    LegalDocument getCitationsById(String decisionType, String year, String id, int req, LegalDocument legald);
    
    /**
     * Get the complete structure of a legal document given its id.
     *
     * @param decisionType the decision type
     * @param year the year
     * @param id the id
     * @param req the request
     * @param legald the ledagl document
     * @return LegalDocument
     */
    LegalDocument getById(String decisionType, String year, String id, int req, LegalDocument legald);
    
    /**
     * Gets RDF graph given a legal document id.
     *
     * @param decisionType the decision type
     * @param year the year
     * @param id the id
     * @return String
     */
    String getRDFById(String decisionType, String year, String id);
    
    /**
     * Executes the SPARQL query given on the Sesame Server with the given format.
     *
     * @param endpointResult the endpoint result
     * @param format the format
     * @return EndpointResultSet
     */
    EndpointResultSet sparqlQuery(EndpointResultSet endpointResult, String format);
    
    /**
     * Gets all modifications of a legad document if they exist for the given id.
     *
     * @param decisionType the decision type
     * @param year the year
     * @param id the id
     * @param date the date
     * @param req the request
     * @return List<Modification>
     */
    List<Modification> getAllModifications(String decisionType, String year, String id, String date, int req);
    
    /**
     * Performs a search given certain params and returns a list matching legal documents.
     *
     * @param params the parameters
     * @return List<LegalDocument>
     */
    List<LegalDocument> search(Map<String, String> params);
    
    /**
     * Gets the tags of a legal document.
     *
     * @return ListM<String>
     */
    List<String> getTags();
    
    /**
     * Gets the most viewed legal documents.
     *
     * @return List<LegalDocument>
     */
    List<LegalDocument> getViewed();
    
    /**
     * Gets the most recent legal documents.
     * @return List<LegalDocument>
     */
    List<LegalDocument> getRecent();
    
    /**
     * Gets a legislation document's type by year order.
     *
     * @return String
     */
    String getLegislationTypeByYear();
    
    /**
     * Gets all Fek statistics.
     *
     * @return List<GovernmentGazette>
     */
    List<GovernmentGazette> getFEKStatistics();
    
    /**
     * Gets all legal documents statistics.
     *
     * @return List<ArrayList<String>>
     */
    List<ArrayList<String>> getStatistics();
}
