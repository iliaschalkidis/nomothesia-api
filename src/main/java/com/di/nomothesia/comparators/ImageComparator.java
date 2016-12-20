package com.di.nomothesia.comparators;

import java.util.Comparator;

/**
 * @author Panagiotis
 */
public class ImageComparator implements Comparator<String> {
    @Override
    public int compare(String a1, String a2) {
        return Integer.parseInt(a1.split("_")[2].split("\\.")[0]) - Integer.parseInt(a2.split("_")[2].split("\\.")[0]);
    }
}