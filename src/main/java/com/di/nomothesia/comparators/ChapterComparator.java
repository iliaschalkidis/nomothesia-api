/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Chapter;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class ChapterComparator implements Comparator<Chapter> {
    @Override
    public int compare(Chapter c1, Chapter c2) {
        
        String[] bits = c1.getURI().split("/");
        String lastOne = bits[bits.length-1];
        
        String[] bits2 = c2.getURI().split("/");
        String lastOne2 = bits2[bits2.length-1];
        
        int i = Integer.parseInt(lastOne);
        int j = Integer.parseInt(lastOne2);
        
        c1.setId(i);
        c2.setId(j);
        
        return i - j;
    }
}