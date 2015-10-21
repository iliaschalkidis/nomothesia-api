/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Article;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class ArticleComparator implements Comparator<Article> {
    @Override
    public int compare(Article a1, Article a2) {
        
//        String[] bits = a1.getURI().split("/");
//        String lastOne = bits[bits.length-1];
//        
//        String[] bits2 = a2.getURI().split("/");
//        String lastOne2 = bits2[bits2.length-1];
//        
//        int i = Integer.parseInt(lastOne);
//        int j = Integer.parseInt(lastOne2);
//        
//        a1.setId(i);
//        a2.setId(j);
        
        return a1.getId() - a2.getId();
    }
}