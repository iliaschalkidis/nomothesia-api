
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class LegalDocument {
     
    private List<Article> articles;
    private List<Signer> signers;
    private List<Citation> citations;
    private List<String> tags;
    
    private String title;
    private String URI;
    private String publicationDate;
    private String FEK;
    private String decisionType;
    private String year;
    private String id;
    
    public LegalDocument() {
        
        articles = new ArrayList<Article>();
        //art.add(new Article());
        signers = new ArrayList<Signer>();
        //sin.add(new Signer());
        citations = new ArrayList<Citation>();
        //cit.add(new Citation());
        this.tags = new ArrayList<String>();

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
    
    public String getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public String getFEK() {
        return FEK;
    }
    
    public void setFEK(String FEK) {
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
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void applyModifications(List<Modification> mods) {
        
        for (Modification mod : mods) {
            String[] hierarchy = mod.getPatient().split("/");
            System.out.println("last"+hierarchy[hierarchy.length-1]);
            if(mod.getType().contains("Edit")){
                if(hierarchy[hierarchy.length-2].equals("passage")){
                    int count3 = Integer.parseInt(hierarchy[hierarchy.length-1]) - 1;
                    int count2 = Integer.parseInt(hierarchy[hierarchy.length-3]) - 1;
                    int count1 = Integer.parseInt(hierarchy[hierarchy.length-5]) - 1;
                    Passage passage = (Passage) mod.getFragment();
                    passage.setId(this.articles.get(count1).getParagraphs().get(count2).getPassages().get(count3).getId());
                    this.articles.get(count1).getParagraphs().get(count2).getPassages().set(count3, passage);
                }
                else if(hierarchy[hierarchy.length-2].equals("case")){
                    int count3 = Integer.parseInt(hierarchy[hierarchy.length-1]) - 1;
                    int count2 = Integer.parseInt(hierarchy[hierarchy.length-3]) - 1;
                    int count1 = Integer.parseInt(hierarchy[hierarchy.length-5]) - 1;
                    Case case1 = (Case) mod.getFragment();
                    case1.setId(this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(count3).getId());
                    this.articles.get(count1).getParagraphs().get(count2).getCaseList().set(count3, case1);
                }
                else if(hierarchy[hierarchy.length-2].equals("paragraph")){
                    int count2 = Integer.parseInt(hierarchy[hierarchy.length-1]) - 1;
                    int count1 = Integer.parseInt(hierarchy[hierarchy.length-3]) - 1;
                    Paragraph paragraph = (Paragraph) mod.getFragment();
                    paragraph.setId(this.articles.get(count1).getParagraphs().get(count2).getId());
                    this.articles.get(count1).getParagraphs().set(count2, paragraph);
                }
            }
            else if(mod.getType().contains("Creation")){
                if(hierarchy[hierarchy.length-2].equals("paragraph")){
                    int count2 = Integer.parseInt(hierarchy[hierarchy.length-1]) - 1;
                    int count1 = Integer.parseInt(hierarchy[hierarchy.length-3]) - 1;
                    if(mod.getFragment().getURI().contains("case")){
                        Case case1 = (Case) mod.getFragment();
                        case1.setId(this.articles.get(count1).getParagraphs().get(count2).getCaseList().size()+1);
                        this.articles.get(count1).getParagraphs().get(count2).getCaseList().add(case1);
                    }
                    else{
                        Passage passage = (Passage) mod.getFragment();
                        passage.setId(this.articles.get(count1).getParagraphs().get(count2).getPassages().size()+1);
                        this.articles.get(count1).getParagraphs().get(count2).getPassages().add(passage);
                    }
                }
            }
        }
        
    }
    
}
