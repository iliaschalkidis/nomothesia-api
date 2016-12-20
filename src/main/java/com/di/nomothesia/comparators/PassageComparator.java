package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Passage;

import java.util.Comparator;

/**
 * @author Panagiotis
 */
public class PassageComparator implements Comparator<Passage> {
    @Override
    public int compare(Passage a1, Passage a2) {
        return a1.getId() - a2.getId();
    }
}