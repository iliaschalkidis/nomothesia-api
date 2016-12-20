package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Chapter;

import java.util.Comparator;

/**
 * @author Panagiotis
 */
public class ChapterComparator implements Comparator<Chapter> {
    @Override
    public int compare(Chapter c1, Chapter c2) {
        return c1.getId() - c2.getId();
    }
}