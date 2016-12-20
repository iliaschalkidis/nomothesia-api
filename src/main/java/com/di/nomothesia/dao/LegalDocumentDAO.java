package com.di.nomothesia.dao;

import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface LegalDocumentDAO {
    
    public LegalDocument getById(String decisionType, String year, String id, int req, LegalDocument legald);
    
    public LegalDocument getCitationsById(String decisionType, String year, String id, int req, LegalDocument legald);
    
    public LegalDocument getMetadataById(String decisionType, String year, String id);
    
    public List<Modification> getAllModifications(String decisionType, String year, String id, String date, int req);
    
    //    public List<LegalDocument> getAllModifications(String decisionType, String year, String id);
    public EndpointResultSet sparqlQuery(EndpointResultSet endpointResult, String format);
    
    public String getRDFById(String decisionType, String year, String id);
    
    public List<LegalDocument> search(Map<String, String> params);
    
    public List<String> getTags();
    
    public List<LegalDocument> getViewed();
    
    public List<LegalDocument> getRecent();
    
    public String getLegislationTypeByYear();
    
    public List<GovernmentGazette> getFEKStatistics();
    
    public List<ArrayList<String>> getStatistics();
    
}
