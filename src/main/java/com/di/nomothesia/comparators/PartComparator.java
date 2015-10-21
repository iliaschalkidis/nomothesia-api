/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Part;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class PartComparator implements Comparator<Part> {
    @Override
    public int compare(Part c1, Part c2) {

        return c1.getId() - c2.getId();
    }
}