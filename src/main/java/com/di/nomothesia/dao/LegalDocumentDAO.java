package com.di.nomothesia.dao;

import com.di.nomothesia.model.EndpointResult;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import java.util.List;


public interface LegalDocumentDAO {
    
    public LegalDocument getById(String decisionType, String year, String id);
    public LegalDocument getMetadataById(String decisionType, String year, String id);
    public List<Modification> getAllModifications(String decisionType, String year, String id);
    public EndpointResult sparqlQuery(EndpointResult endpointResult);
    public String getRDFById(String decisionType, String year, String id);
}
