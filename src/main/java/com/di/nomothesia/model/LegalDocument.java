
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LegalDocument {
     
    private List<Article> articles;
    private List<Signer> signers;
    private List<Citation> citations;
    private String title;
    private String URI;
    private Date publicationDate;
    private GovernmentGazette FEK;
    private String decisionType;
    private String year;
    private String id;
    
    public LegalDocument() {
                ArrayList<Article> art = new ArrayList<Article>();
                art.add(new Article());
		ArrayList<Signer> sin = new ArrayList<Signer>();
                sin.add(new Signer());
                ArrayList<Citation> cit = new ArrayList<Citation>();
                cit.add(new Citation());
                title = "Ο ΤΕΛΕΥΤΑΙΟΣ ΤΩΝ ΤΡΟΪΚΑΝΩΝ";
                URI = "legislation/legaldocument/skata";
                FEK = new GovernmentGazette();
                decisionType = "ελλας-ελλας";
                year = "2014";
                id = "56";
                
                System.out.println("Είμαι το νομικό έγγραφο:" + title + id + URI + decisionType + year);
    }
    
    //Setters-Getters for LegalDocument

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Signer> getSigners() {
        return signers;
    }

    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    public List<Citation> getCitations() {
        return citations;
    }

    public void setCitations(List<Citation> citations) {
        this.citations = citations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public GovernmentGazette getFEK() {
        return FEK;
    }

    public void setFEK(GovernmentGazette FEK) {
        this.FEK = FEK;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
