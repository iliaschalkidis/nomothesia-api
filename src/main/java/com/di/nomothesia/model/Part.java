
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class Part implements Fragment{
    
    private String Title;
    private int id;
    private String URI;
    List<Article> articles;
    List<Chapter> chapters;
    private int status;
    private String type;
    
    public Part() {
        this.chapters = new ArrayList<Chapter>();
        this.articles = new ArrayList<Article>();
        status = 0;
    }
    
    //Setters-Getters for Article
    @Override
    public void setType(String t) {
        this.type = t;
    }

    @Override
    public String getType() {
        return type;
    }
    
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
    
    public List<Article> getArticles() {
        return articles;
    }
    
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
    
    public List<Chapter> getChapters() {
        return chapters;
    }
    
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int s) {
        this.status = s;
    }
    
}

