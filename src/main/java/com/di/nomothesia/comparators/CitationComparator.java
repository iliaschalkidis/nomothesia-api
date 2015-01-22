/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Citation;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class CitationComparator implements Comparator<Citation> {
    @Override
    public int compare(Citation a1, Citation a2) {
        String[] bits = a1.getURI().split("/");
        String lastOne = bits[bits.length-1];
        
        String[] bits2 = a2.getURI().split("/");
        String lastOne2 = bits2[bits2.length-1];
        
        int i = Integer.parseInt(lastOne);
        int j = Integer.parseInt(lastOne2);
        
        return i - j;
    }
}
