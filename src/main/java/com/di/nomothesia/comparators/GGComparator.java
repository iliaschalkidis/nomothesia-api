/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.Passage;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class GGComparator implements Comparator<GovernmentGazette> {
    @Override
    public int compare(GovernmentGazette a1, GovernmentGazette a2) {
  
        return a1.getId() - a2.getId();
    }
}