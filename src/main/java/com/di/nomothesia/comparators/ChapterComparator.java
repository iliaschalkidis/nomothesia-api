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

        return c1.getId() - c2.getId();
    }
}