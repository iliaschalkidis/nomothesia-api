/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Case;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class CaseComparator implements Comparator<Case> {
    @Override
    public int compare(Case a1, Case a2) {
        
        return a1.getId() - a2.getId();
    }
}