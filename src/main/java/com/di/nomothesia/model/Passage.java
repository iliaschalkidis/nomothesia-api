
package com.di.nomothesia.model;

public class Passage implements Fragment {
   
    private String uri;
    private String text;
    private int id;
    private int status;
    private String type;
    private Modification modification;

    public Passage() {

    }
    
    //Setters-Getters for Passage
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

    public String getText() {
        return text;
    }
   
    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Modification getModification() {
        return modification;
    }

    public void setModification(Modification modification) {
        this.modification = modification;
    }

    @Override
    public void setType(String t) {
        this.type = t;
    }

    @Override
    public String getType() {
        return type;
    }
    
}
