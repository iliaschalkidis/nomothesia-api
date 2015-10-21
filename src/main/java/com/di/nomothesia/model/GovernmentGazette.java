
package com.di.nomothesia.model;

import java.util.ArrayList;

public class GovernmentGazette {
    
    private String URI;
    private int docs;
    private String issue;
    private String year;
    private int id;
    private String fileName;
    private String title;
    private ArrayList<LegalDocument> documents;
    private String date;
    private int issues;
    public GovernmentGazette() {
        
    }
    
    //Setters-Getters for GovernmentGazette
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getURI() {
        return URI;
    }

    public int getDocs() {
        return docs;
    }
    
    public int getIssues() {
        return issues;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }


    public void setDocs(int docs) {
        this.docs = docs;
    }
    
    public void setIssues(int issues) {
        this.issues = issues;
    }

    public void setList(ArrayList<LegalDocument> lds) {
        this.documents = lds;
    }
    
     public ArrayList<LegalDocument> getList() {
        return this.documents;
    }

    public void setPublicationDate(String publicationDate) {
        this.date= publicationDate;
    }

    public ArrayList<LegalDocument> getDocuments() {
        return documents;
    }

    public String getPublicationDate() {
        return date;
    }
}
