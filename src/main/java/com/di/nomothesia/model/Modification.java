
package com.di.nomothesia.model;

public class Modification {
    
    private Fragment fragment;
    private String uri;
    private String type;
    private String patient;
    private LegalDocument competenceGround;
    
    public Modification() {
        this.competenceGround = new LegalDocument();
    }
    
    //Setters-Getters for Modification
    
    public LegalDocument getCompetenceGround() {
        return competenceGround;
    }
    
    public String getPatient() {
        return patient;
    }
    
    public void setPatient(String patient) {
        this.patient = patient;
    }
    
    public Fragment getFragment() {
        return fragment;
    }
    
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
    
    public String getURI() {
        return uri;
    }
    
    public String getTarget() {
        String target = patient.split("(law|pd|la|rd|con|md|rp|amc)+\\/[0-9]+\\/")[1].split("\\/", 2)[1];
        return target;
    }
    
    public void setURI(String uri) {
        this.uri = uri;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
}
