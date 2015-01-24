/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.LegalDocument;
import java.util.Collections;

/**
 *
 * @author Panagiotis
 */
public class LegalDocumentSort {
    
    public LegalDocument sortld (LegalDocument legald) {
        
        Collections.sort(legald.getArticles(), new ArticleComparator());
        
        //for all articles
        for (int i=0;i<legald.getArticles().size();i++) {
            
            Collections.sort(legald.getArticles().get(i).getParagraphs(), new ParagraphComparator());
            
            //for all pargraphs
            for (int j=0;j<legald.getArticles().get(i).getParagraphs().size();j++) {
                
                Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getPassages(), new PassageComparator());
                
                if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList() != null) {
                    
                    Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getCaseList(), new CaseComparator());
                    
                    //for all cases
                    for(int k=0;k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size();k++) {
                    
                        if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages() != null) {
                        
                             //Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getCaseList()get(k).getPassages(), new PassageComparator());
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
       
        return legald;
    
    }
}
