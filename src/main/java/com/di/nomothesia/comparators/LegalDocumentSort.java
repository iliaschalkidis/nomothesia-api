/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.comparators;

import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Paragraph;
import java.util.Collections;

/**
 *
 * @author Panagiotis
 */
public class LegalDocumentSort {
    
    public LegalDocument sortld (LegalDocument legald) {
        
        Collections.sort(legald.getCitations(), new CitationComparator());
        Collections.sort(legald.getImages(), new ImageComparator());
        if(!legald.getParts().isEmpty()){
            Collections.sort(legald.getParts(), new PartComparator());
            for (int o=0;o<legald.getParts().size();o++) {
                if(legald.getParts().get(o).getChapters().isEmpty()){
                        Collections.sort(legald.getParts().get(o).getArticles(), new ArticleComparator());
                         //for all articles
                        for (int i=0;i<legald.getParts().get(o).getArticles().size();i++) {

                            Collections.sort(legald.getParts().get(o).getArticles().get(i).getParagraphs(), new ParagraphComparator());

                            //for all pargraphs
                            for (int j=0;j<legald.getParts().get(o).getArticles().get(i).getParagraphs().size();j++) {

                                Collections.sort(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages(), new PassageComparator());

                                for (int z=0;z<legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().size();z++) {
                                    if (legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification() != null) {
                                        if(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Article")){
                                            Article art = (Article) legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                            Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                            for (int l=0;l<art.getParagraphs().size();l++) {
                                                Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                                if (art.getParagraphs().get(l).getCaseList() != null) {
                                                    Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                                }
                                            }
                                            legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(art);
                                        }
                                        else if(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Paragraph")){
                                            Paragraph par= (Paragraph) legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                            Collections.sort(par.getPassages(), new PassageComparator());
                                            if (par.getCaseList() != null) {
                                                Collections.sort(par.getCaseList(), new CaseComparator());
                                            }
                                            legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(par);
                                        }
                                    }
                                }

                                if (legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList() != null) {

                                    Collections.sort(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList(), new CaseComparator());

                                    //for all cases
                                    for(int k=0;k<legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().size();k++) {
                                        if (legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {
                                            if(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")){
                                                Article art = (Article) legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                                Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                                for (int l=0;l<art.getParagraphs().size();l++) {
                                                    Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                                    if (art.getParagraphs().get(l).getCaseList() != null) {
                                                        Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                                    }
                                                }
                                                legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(art);
                                            }
                                            else if(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")){
                                                Paragraph par= (Paragraph) legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                                Collections.sort(par.getPassages(), new PassageComparator());
                                                if (par.getCaseList() != null) {
                                                    Collections.sort(par.getCaseList(), new CaseComparator());
                                                }
                                                legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(par);
                                            }
                                        }
            //                            if (legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages() != null) {
            //
            //                                 //Collections.sort(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList()get(k).getPassages(), new PassageComparator());
            //
            //                            }

                                    }

                                }

                            }

                        }
                    }
                    else{
                        Collections.sort(legald.getParts().get(o).getChapters(), new ChapterComparator());
                        for (int w=0;w<legald.getParts().get(o).getChapters().size();w++) {
                            Collections.sort(legald.getParts().get(o).getChapters().get(w).getArticles(), new ArticleComparator());
                            //for all articles
                            for (int i=0;i<legald.getParts().get(o).getChapters().get(w).getArticles().size();i++) {

                                Collections.sort(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs(), new ParagraphComparator());

                                //for all pargraphs
                                for (int j=0;j<legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().size();j++) {

                                    Collections.sort(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages(), new PassageComparator());

                                    for (int z=0;z<legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().size();z++) {
                                        if (legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification() != null) {
                                            if(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Article")){
                                                Article art = (Article) legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                                Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                                for (int l=0;l<art.getParagraphs().size();l++) {
                                                    Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                                    if (art.getParagraphs().get(l).getCaseList() != null) {
                                                        Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                                    }
                                                }
                                                legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(art);
                                            }
                                            else if(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Paragraph")){
                                                Paragraph par= (Paragraph) legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                                Collections.sort(par.getPassages(), new PassageComparator());
                                                if (par.getCaseList() != null) {
                                                    Collections.sort(par.getCaseList(), new CaseComparator());
                                                }
                                                legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(par);
                                            }
                                        }
                                    }

                                    if (legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList() != null) {

                                        Collections.sort(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList(), new CaseComparator());

                                        //for all cases
                                        for(int k=0;k<legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().size();k++) {
                                            if (legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {
                                                if(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")){
                                                    Article art = (Article) legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                                    Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                                    for (int l=0;l<art.getParagraphs().size();l++) {
                                                        Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                                        if (art.getParagraphs().get(l).getCaseList() != null) {
                                                            Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                                        }
                                                    }
                                                    legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(art);
                                                }
                                                else if(legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")){
                                                    Paragraph par= (Paragraph) legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                                    Collections.sort(par.getPassages(), new PassageComparator());
                                                    if (par.getCaseList() != null) {
                                                        Collections.sort(par.getCaseList(), new CaseComparator());
                                                    }
                                                    legald.getParts().get(o).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(par);
                                                }
                                            }
                //                            if (legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages() != null) {
                //
                //                                 //Collections.sort(legald.getParts().get(o).getArticles().get(i).getParagraphs().get(j).getCaseList()get(k).getPassages(), new PassageComparator());
                //
                //                            }

                                        }

                                    }

                                }

                            }
                        }
                    }
            }

        }
        else{
            if(legald.getChapters().isEmpty()){
                Collections.sort(legald.getArticles(), new ArticleComparator());
                 //for all articles
                for (int i=0;i<legald.getArticles().size();i++) {

                    Collections.sort(legald.getArticles().get(i).getParagraphs(), new ParagraphComparator());

                    //for all pargraphs
                    for (int j=0;j<legald.getArticles().get(i).getParagraphs().size();j++) {

                        Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getPassages(), new PassageComparator());

                        for (int z=0;z<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size();z++) {
                            if (legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification() != null) {
                                if(legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Article")){
                                    Article art = (Article) legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                    Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                    for (int l=0;l<art.getParagraphs().size();l++) {
                                        Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                        if (art.getParagraphs().get(l).getCaseList() != null) {
                                            Collections.sort(art.getParagraphs().get(j).getCaseList(), new CaseComparator());
                                        }
                                    }
                                    legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(art);
                                }
                                else if(legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Paragraph")){
                                    Paragraph par= (Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                    Collections.sort(par.getPassages(), new PassageComparator());
                                    if (par.getCaseList() != null) {
                                        Collections.sort(par.getCaseList(), new CaseComparator());
                                    }
                                    legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(par);
                                }
                            }
                        }

                        if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList() != null) {

                            Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getCaseList(), new CaseComparator());

                            //for all cases
                            for(int k=0;k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size();k++) {
                                if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {
                                    if(legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")){
                                        Article art = (Article) legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                        Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                        for (int l=0;l<art.getParagraphs().size();l++) {
                                            Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                            if (art.getParagraphs().get(l).getCaseList() != null) {
                                                Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                            }
                                        }
                                        legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(art);
                                    }
                                    else if(legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")){
                                        Paragraph par= (Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                        Collections.sort(par.getPassages(), new PassageComparator());
                                        if (par.getCaseList() != null) {
                                            Collections.sort(par.getCaseList(), new CaseComparator());
                                        }
                                        legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(par);
                                    }
                                }
    //                            if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages() != null) {
    //
    //                                 //Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getCaseList()get(k).getPassages(), new PassageComparator());
    //
    //                            }

                            }

                        }

                    }

                }
            }
            else{
                Collections.sort(legald.getChapters(), new ChapterComparator());
                for (int w=0;w<legald.getChapters().size();w++) {
                    Collections.sort(legald.getChapters().get(w).getArticles(), new ArticleComparator());
                    //for all articles
                    for (int i=0;i<legald.getChapters().get(w).getArticles().size();i++) {

                        Collections.sort(legald.getChapters().get(w).getArticles().get(i).getParagraphs(), new ParagraphComparator());

                        //for all pargraphs
                        for (int j=0;j<legald.getChapters().get(w).getArticles().get(i).getParagraphs().size();j++) {

                            Collections.sort(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages(), new PassageComparator());

                            for (int z=0;z<legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().size();z++) {
                                if (legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification() != null) {
                                    if(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Article")){
                                        Article art = (Article) legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                        Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                        for (int l=0;l<art.getParagraphs().size();l++) {
                                            Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                            if (art.getParagraphs().get(l).getCaseList() != null) {
                                                Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                            }
                                        }
                                        legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(art);
                                    }
                                    else if(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getType().equals("Paragraph")){
                                        Paragraph par= (Paragraph) legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().getFragment();
                                        Collections.sort(par.getPassages(), new PassageComparator());
                                        if (par.getCaseList() != null) {
                                            Collections.sort(par.getCaseList(), new CaseComparator());
                                        }
                                        legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(z).getModification().setFragment(par);
                                    }
                                }
                            }

                            if (legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList() != null) {

                                Collections.sort(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList(), new CaseComparator());

                                //for all cases
                                for(int k=0;k<legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().size();k++) {
                                    if (legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {
                                        if(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")){
                                            Article art = (Article) legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                            Collections.sort(art.getParagraphs(), new ParagraphComparator());
                                            for (int l=0;l<art.getParagraphs().size();l++) {
                                                Collections.sort(art.getParagraphs().get(l).getPassages(), new PassageComparator());
                                                if (art.getParagraphs().get(l).getCaseList() != null) {
                                                    Collections.sort(art.getParagraphs().get(l).getCaseList(), new CaseComparator());
                                                }
                                            }
                                            legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(art);
                                        }
                                        else if(legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")){
                                            Paragraph par= (Paragraph) legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();
                                            Collections.sort(par.getPassages(), new PassageComparator());
                                            if (par.getCaseList() != null) {
                                                Collections.sort(par.getCaseList(), new CaseComparator());
                                            }
                                            legald.getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().setFragment(par);
                                        }
                                    }
        //                            if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages() != null) {
        //
        //                                 //Collections.sort(legald.getArticles().get(i).getParagraphs().get(j).getCaseList()get(k).getPassages(), new PassageComparator());
        //
        //                            }

                                }

                            }

                        }

                    }
                }
            }
        }
       
        return legald;
    
    }
}
