
package com.di.nomothesia.model;

public class Citation {
        
    private String description;
    private String URI;
    
    public Citation() {
		URI = "legislation/citation/skata";
		description = "ÄÓÖÓÇÖ Ç ÇÖÄÎÓÖ ÎÇ ÊÇÖ ÊÎÇ ÊÓÄ";
		
                System.out.println("Åßìáé ç ğáñÜèåóç:" + URI + description);
    }
    
    //Setters-Getters for Citation
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

}
