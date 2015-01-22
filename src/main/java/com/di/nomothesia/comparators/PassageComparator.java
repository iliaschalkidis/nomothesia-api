/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Passage;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class PassageComparator implements Comparator<Passage> {
    @Override
    public int compare(Passage a1, Passage a2) {
        
        String[] bits = a1.getURI().split("/");
        String lastOne = bits[bits.length-1];
        
        String[] bits2 = a2.getURI().split("/");
        String lastOne2 = bits2[bits2.length-1];
        
        int i = Integer.parseInt(lastOne);
        int j = Integer.parseInt(lastOne2);
        
        a1.setId(i);
        a2.setId(j);
        
        return i - j;
    }
}