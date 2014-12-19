
package com.di.nomothesia.model;

public class Passage implements Fragment {
    
    private String URI;
    private String text;
    private int id;
    
    public Passage() {
//		URI = "legislation/passage/skata";
//		text = "буудко то кокуодобкуо ткодбкдоко";
//		id = 2;
//                
//                System.out.println("≈ямбй фп ед№цйп:" + URI + text + id);
    }
    
    //Setters-Getters for Passage

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
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
    
}
