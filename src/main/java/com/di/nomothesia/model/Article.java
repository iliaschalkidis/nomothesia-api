
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class Article implements Fragment{
    
    private String Title;
    private int id;
    private String URI;
    List<Paragraph> paragraphs;
    
    public Article() {
        
        this.paragraphs = new ArrayList<Paragraph>();
    
    }
    
    //Setters-Getters for Article
    
    public String getTitle() {
        return Title;
    }
    
    public void setTitle(String Title) {
        this.Title = Title;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String getURI() {
        return URI;
    }
    
    public void setURI(String URI) {
        this.URI = URI;
    }
    
    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
    
    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
    
}
