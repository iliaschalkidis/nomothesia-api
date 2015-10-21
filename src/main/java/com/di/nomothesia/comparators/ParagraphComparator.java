/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Paragraph;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class ParagraphComparator implements Comparator<Paragraph> {
    @Override
    public int compare(Paragraph a1, Paragraph a2) {
        return a1.getId() - a2.getId();
    }
}
