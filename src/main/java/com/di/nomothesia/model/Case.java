
package com.di.nomothesia.model;

import java.util.ArrayList;
import java.util.List;

public class Case implements Fragment {
    
    private String uri;
    private int id;
    private List<Passage> passages;
    private List<Case> caseList;
    private int status;
    private String type;
    private Modification modification;
    
    public Case() {
        
        passages = new ArrayList<>();
        //pas.add(new Passage());
        caseList = new ArrayList<>();
        //casel.add(new Case());
        
        
    }
    
    //Setters-Getters for Case
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
    
    @Override
    public String getURI() {
        return uri;
    }
    
    public void setURI(String uri) {
        this.uri = uri;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public Modification getModification() {
        return modification;
    }
    
    public void setModification(Modification modification) {
        this.modification = modification;
    }
    
    
}
