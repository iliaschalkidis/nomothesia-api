
package com.di.nomothesia.model;

import java.util.ArrayList;

public class GovernmentGazette {
    
    private String issue;
    private String year;
    private String id;
    
    public GovernmentGazette() {
                issue = "�������� � �����";
                year = "2014";
                id = "23";
                
                System.out.println("����� � ���������:" + year + issue + id);
    }
    
    //Setters-Getters for GovernmentGazette
    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
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
      
}
