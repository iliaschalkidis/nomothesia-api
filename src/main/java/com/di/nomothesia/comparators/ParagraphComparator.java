/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.Paragraph;
import java.util.Comparator;

/**
 *
 * @author Panagiotis
 */
public class ParagraphComparator implements Comparator<Paragraph> {
    @Override
    public int compare(Paragraph a1, Paragraph a2) {
        int xflag = 0;
        int yflag = 0;
        int x = 0;
        int y = 0;
//        String[] bits = a1.getURI().split("/");
//        String lastOne = bits[bits.length-1];
//        
//        String[] bits2 = a2.getURI().split("/");
//        String lastOne2 = bits2[bits2.length-1];
//        
//        int i = Integer.parseInt(lastOne);
//        int j = Integer.parseInt(lastOne2);
//        
//        a1.setId(i);
//        a2.setId(j);
        try{
            x = Integer.parseInt(a1.getId());
        }
        catch(NumberFormatException e){
            xflag = 1;
            x = Integer.parseInt(a1.getId().substring(0, a1.getId().length()-1));
        }
        try{
            y = Integer.parseInt(a2.getId());
        }
        catch(NumberFormatException e){
            yflag = 1;
            y = Integer.parseInt(a2.getId().substring(0, a2.getId().length()-1));
        }
        
        int dif = x-y;
        if(dif!=0){
            return dif;
        }
        else{
            if(xflag==1&&yflag==1){
                return a1.getId().charAt(a1.getId().length()) - a2.getId().charAt(a2.getId().length());
            }
            else if(xflag==1){
                return 1;
            }
            else{
                return -1;
            }
        }
    }
}
