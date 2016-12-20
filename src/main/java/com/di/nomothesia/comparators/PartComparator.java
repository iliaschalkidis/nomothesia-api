package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Part;

import java.util.Comparator;

/**
 * @author Panagiotis
 */
public class PartComparator implements Comparator<Part> {
    @Override
    public int compare(Part c1, Part c2) {
        return c1.getId() - c2.getId();
    }
}