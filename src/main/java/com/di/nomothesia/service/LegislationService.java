package com.di.nomothesia.service;

import com.di.nomothesia.model.*;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by psour on 14/11/2016.
 *
 */
public interface LegislationService {
    /**
     * Get legal document by id.
     *
     * @param decisionType the decisionType
     * @param year         year
     * @param id           id
     * @param request      request
     * @return LegalDocument
     */
    LegalDocument getById(String decisionType, String year, String id, int request);

    /**
     * Get all modifications by legal document id.
     *
     * @param type    the type
     * @param year    the year
     * @param id      the id
     * @param request the request
     * @param date    the date
     * @return List<Modification>
     */
    List<Modification> getAllModificationsById(String type, String year, String id, int request, String date);

    /**
     * Get the resultset from sparql query.
     *
     * @param query  the query
     * @param format the format
     * @return EndpointResultSet
     */
    EndpointResultSet sparqlQuery(String query, String format);

    /**
     * Get rdf graph by id.
     *
     * @param type the type
     * @param year the year
     * @param id   the id
     * @return String
     */
    String getRDFById(String type, String year, String id);

    /**
     * Get xml by id.
     *
     * @param type    the type
     * @param year    the year
     * @param id      the id
     * @param request the request
     * @return String
     */
    String getXMLById(String type, String year, String id, int request) throws TransformerException;

    /**
     * Get list of legal documents based on params.
     *
     * @param params the params
     * @return List<LegalDocument>
     */
    List<LegalDocument> searchLegislation(Map<String, String> params);
    
    /**
     * Get most viewd legal documents.
     *
     * @return List<LegalDocument>
     */
    List<LegalDocument> mostViewed();

    /**
     * Get most recent legal documents.
     *
     * @return List<LegalDocument>
     */
    List<LegalDocument> mostRecent();

    /**
     * Get tags of legal document.
     *
     * @return List<String>
     */
    List<String> getTags();

    /**
     * Get updated fragments by id.
     *
     * @param legald the legald
     * @param mods   the mods
     * @return List<Fragment>
     */
    List<Fragment> getUpdatedById(LegalDocument legald, List<Modification> mods);

    /**
     * Get latest version of legal document in xml by id.
     *
     * @param type    the type
     * @param year    the year
     * @param id      the id
     * @param request the request
     * @return String
     */
    String getUpdatedXMLById(String type, String year, String id, int request) throws TransformerException;

    /**
     * Get latest version of legal document in xml by id and date.
     *
     * @param type    the type
     * @param year    the year
     * @param id      the id
     * @param request the request
     * @param date    the date
     * @return String
     */
    String getUpdatedXMLByIdDate(String type, String year, String id, int request, String date) throws TransformerException;

    /**
     * Get fek statistics.
     *
     * @return List<GovernmentGazette>
     */
    List<GovernmentGazette> getFEKStatistics();

    /**
     * Get statistics of legal document
     *
     * @return List<ArrayListString>
     */
    List<ArrayList<String>> getStats();
}
