package com.di.nomothesia.comparators;

import com.di.nomothesia.model.GovernmentGazette;
import com.di.nomothesia.model.Passage;

import java.util.Comparator;

/**
 * @author Panagiotis
 */
public class GGComparator implements Comparator<GovernmentGazette> {
    @Override
    public int compare(GovernmentGazette a1, GovernmentGazette a2) {
        return a1.getId() - a2.getId();
    }
}