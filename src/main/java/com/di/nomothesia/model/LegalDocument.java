
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class LegalDocument {
    
    private List<Article> articles;
    private List<Chapter> chapters;
    private List<Part> parts;
    private List<String> issues;
    private List<Signer> signers;
    private List<Citation> citations;
    private List<String> tags;
    private List<String> images;
    private String title;
    private String uri;
    private String publicationDate;
    private String fek;
    private String fekFile;
    private String decisionType;
    private String year;
    private String id;
    private String views;
    private String place;
    
    public LegalDocument() {
        
        articles = new ArrayList<>();
        chapters = new ArrayList<>();
        parts = new ArrayList<>();
        signers = new ArrayList<>();
        citations = new ArrayList<>();
        images = new ArrayList<>();
        issues = new ArrayList<>();
        this.tags = new ArrayList<>();
        
    }
    
    //Setters-Getters for LegalDocument
    
    public List<String> getIssues() {
        return issues;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public List<Part> getParts() {
        return parts;
    }
    
    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
    
    public List<Chapter> getChapters() {
        return chapters;
    }
    
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
    
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
    
    public String getPlace() {
        return place;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getURI() {
        return uri;
    }
    
    public void setURI(String uri) {
        this.uri = uri;
    }
    
    public String getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public String getFEKfile() {
        return fekFile;
    }
    
    public void setFEKfile(String fekFile) {
        this.fekFile = fekFile;
    }
    
    public String getFEK() {
        return fek;
    }
    
    public void setFEK(String fek) {
        this.fek = fek;
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
    
    public String getViews() {
        return views;
    }
    
    public void setViews(String views) {
        this.views = views;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<Fragment> applyModifications(List<Modification> mods) {
        List<Fragment> frags = new ArrayList<Fragment>();
        int issue_flag = 0;
        for (Modification mod : mods) {
            String[] hierarchy = mod.getPatient().split("/");
            System.out.println("last" + hierarchy[hierarchy.length - 1]);
            try {
                if (mod.getType().contains("Edit")) {
                    if (hierarchy[hierarchy.length - 2].equals("passage")) {
                        int count3 = Integer.parseInt(hierarchy[hierarchy.length - 1]) - 1;
                        int count2 = Integer.parseInt(hierarchy[hierarchy.length - 3]) - 1;
                        int count1 = Integer.parseInt(hierarchy[hierarchy.length - 5]) - 1;
                        Passage passage = (Passage) mod.getFragment();
                        passage.setId(this.articles.get(count1).getParagraphs().get(count2).getPassages().get(
                                count3).getId());
                        frags.add(this.articles.get(count1).getParagraphs().get(count2).getPassages().get(count3));
                        String URI = this.articles.get(count1).getParagraphs().get(count2).getPassages().get(
                                count3).getURI();
                        this.articles.get(count1).getParagraphs().get(count2).getPassages().set(count3, passage);
                        this.articles.get(count1).getParagraphs().get(count2).getPassages().get(count3).setURI(URI);
                        mod.setURI(URI);
                    } else if (hierarchy[hierarchy.length - 2].equals("case")) {
                        int count3 = Integer.parseInt(hierarchy[hierarchy.length - 1]) - 1;
                        int count2 = Integer.parseInt(hierarchy[hierarchy.length - 3]) - 1;
                        int count1 = Integer.parseInt(hierarchy[hierarchy.length - 5]) - 1;
                        Case case1 = (Case) mod.getFragment();
                        case1.setId(this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(
                                count3).getId());
                        frags.add(this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(count3));
                        String URI = this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(
                                count3).getURI();
                        this.articles.get(count1).getParagraphs().get(count2).getCaseList().set(count3, case1);
                        this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(count3).setURI(URI);
                        mod.setURI(URI);
                    } else if (hierarchy[hierarchy.length - 2].equals("paragraph")) {
                        int count2 = Integer.parseInt(hierarchy[hierarchy.length - 1]) - 1;
                        int count1 = Integer.parseInt(hierarchy[hierarchy.length - 3]) - 1;
                        Paragraph paragraph = (Paragraph) mod.getFragment();
                        paragraph.setId(this.articles.get(count1).getParagraphs().get(count2).getId());
                        frags.add(this.articles.get(count1).getParagraphs().get(count2));
                        String URI = this.articles.get(count1).getParagraphs().get(count2).getURI();
                        this.articles.get(count1).getParagraphs().set(count2, paragraph);
                        this.articles.get(count1).getParagraphs().get(count2).setURI(URI);
                        mod.setURI(URI);
                    }
                } else if (mod.getType().contains("Addition")) {
                    if (hierarchy[hierarchy.length - 2].equals("paragraph")) {
                        int count2 = Integer.parseInt(hierarchy[hierarchy.length - 1]) - 1;
                        int count1 = Integer.parseInt(hierarchy[hierarchy.length - 3]) - 1;
                        if (mod.getFragment().getURI().contains("case")) {
                            Case case1 = (Case) mod.getFragment();
                            int size = this.articles.get(count1).getParagraphs().get(count2).getCaseList().size();
                            case1.setId(this.articles.get(count1).getParagraphs().get(count2).getCaseList().size() + 1);
                            this.articles.get(count1).getParagraphs().get(count2).getCaseList().add(case1);
                            mod.setURI(this.articles.get(count1).getParagraphs().get(count2).getCaseList().get(
                                    size - 1).getURI());
                            mod.setURI(mod.getURI().substring(1, mod.getURI().length() - 1) + size);
                        } else {
                            Passage passage = (Passage) mod.getFragment();
                            int size = this.articles.get(count1).getParagraphs().get(count2).getPassages().size();
                            passage.setId(size + 1);
                            this.articles.get(count1).getParagraphs().get(count2).getPassages().add(passage);
                            mod.setURI(this.articles.get(count1).getParagraphs().get(count2).getPassages().get(
                                    size - 1).getURI());
                            mod.setURI(mod.getURI().substring(1, mod.getURI().length() - 1) + size);
                        }
                    }
                }
            } catch (Exception e) {
                if (issue_flag == 0) {
                    issues.add("mods");
                    issue_flag = 1;
                }
            }
            
        }
        return frags;
    }
    
    public void setPlace(String geometry) {
        this.place = geometry;
    }
    
}
