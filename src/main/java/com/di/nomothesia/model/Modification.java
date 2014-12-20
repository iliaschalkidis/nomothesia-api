
package com.di.nomothesia.model;

public class Modification {
    
    private Fragment fragment;
    private String URI;
    private String Type;
    
    public Modification() {
//                URI = "legislation/mod/skata";
//                Type = "αλλαγή";
//                
//                System.out.println("Είμαι η μόντα:" + URI + Type);
    }
    
    //Setters-Getters for Modification

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
}
