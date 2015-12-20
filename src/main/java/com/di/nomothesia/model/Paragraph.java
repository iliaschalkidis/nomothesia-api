
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements Fragment{
    
    private List<Passage> passages;
    private List<Case> caseList;
    private List<String> images;
    private String table;
    private String URI;
    private String id;
    private int status;
    private String type;
    
    public Paragraph() {
        
        this.passages = new ArrayList<Passage>();
        //pass.add(new Passage());
        this.caseList = new ArrayList<Case>();
        this.images = new ArrayList<String>();
        //cas.add(new Case());
        
    }
    
    //Setters-Getters for Paragraph
    @Override
    public void setType(String t) {
        this.type = t;
    }

    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public int getStatus() {
        return status;
    }
    
    @Override
    public void setStatus(int s) {
        this.status = s;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public List<Passage> getPassages() {
        return passages;
    }

    public void setPassages(List<Passage> passages) {
        this.passages = passages;
    }

    public List<Case> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<Case> caseList) {
        this.caseList = caseList;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
    
    @Override
    public String getURI() {
        return URI;
    }

    public void setURI(String uri) {
        this.URI = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
