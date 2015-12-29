package com.di.nomothesia.controller;

import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Passage;
import com.di.nomothesia.service.AbstractITextPdfView;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PDFBuilder3 extends AbstractITextPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
                String chap_header ="";
                String article_header ="";
                String citation_header ="";
                String city = "";
                String decide = "";
                String type = "";
                String num_header = "";
                String part_header ="";
                Properties props = new Properties();
                InputStream fis = null;
                String[] letter = null;
                String[] quotes = null;
                String[] chap_letters = null;
                // Get data model which is passed by the Controller
		LegalDocument legald = (LegalDocument) model.get("legaldocument");
                try {

                    fis = getClass().getResourceAsStream("/messages_el_GR.properties");
                    props.load(fis);

                    // get the properties values
                    chap_header = props.getProperty("basic.chapter");
                    part_header = props.getProperty("basic.part");
                    article_header = props.getProperty("basic.article");
                    citation_header = props.getProperty("basic.mind");
                    city = props.getProperty("basic.athens");
                    decide = props.getProperty("basic.decide");
                    type = props.getProperty("home."+legald.getDecisionType());
                    num_header = props.getProperty("basic.numh");
                    letter = props.getProperty("basic.smallionian").split(",");
                    chap_letters = props.getProperty("basic.capitalionian").split(",");
                    quotes = props.getProperty("basic.quotes").split(",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Doc margins
                //doc.setMargins(60, 60, 20, 20);
                
                //Add image on top
                String relativeWebPath = "/resources/images/banner.png";
                String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                Image img = Image.getInstance(absoluteDiskPath);
                img.scalePercent(35f);
                img.setAlignment(Element.ALIGN_CENTER);
                doc.add(img);
                
                //Add line seperator
//                LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -45);
//                doc.add(new Chunk(line));
                doc.add(new Paragraph("\n"));
                
                //Fonts
                BaseFont bf = BaseFont.createFont(getServletContext().getRealPath("/resources/fonts/arial.ttf"),BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                bf.setSubset(true);
                Font fontTitle = new Font(bf, 16,Font.BOLD);
                Font fontText = new Font(bf, 10);
                Font citationText = new Font(bf, 10);
                Font fontArticle = new Font(bf, 12,Font.BOLD);
                Font fontDate = new Font(bf, 12);
                Font fontType = new Font(bf, 16,Font.BOLD);
                Font fontSignerf = new Font(bf, 11);
                
                //Type
                String tid = legald.getId();
                tid = trimDoubleQuotes(tid);
                type = trimDoubleQuotes(type);
                type += " "+num_header+" " + tid;
                Paragraph typ = new Paragraph(type, fontType);
                typ.setAlignment(Element.ALIGN_CENTER);
                doc.add(typ);
                doc.add(new Paragraph("\n"));
                
                //Title
                String title = legald.getTitle();
                title = trimDoubleQuotes(title);
                Paragraph titl = new Paragraph(title, fontTitle);
                titl.setAlignment(Element.ALIGN_CENTER);
                doc.add(titl);
                doc.add(new Paragraph("\n"));
                
                //Citations
                if(!legald.getCitations().isEmpty()){
                    String citation = citation_header+" \n\n";
                    Paragraph cit1 = new Paragraph(citation, fontText);
                    cit1.setAlignment(Element.ALIGN_LEFT);
                    String citation2 = "";

                    for (int i=0; i<legald.getCitations().size();i++){
                        citation2 += legald.getCitations().get(i).getId() + ". " + legald.getCitations().get(i).getDescription() + "\n\n";
                    }

                    Paragraph cit2 = new Paragraph(citation2, citationText);
                    cit2.setAlignment(Element.ALIGN_JUSTIFIED);

                    String citation3 = decide+" \n";
                    Paragraph cit3 = new Paragraph(citation3, fontText);
                    cit3.setAlignment(Element.ALIGN_CENTER);
                    doc.add(cit1);
                    doc.add(cit2);
                    doc.add(cit3);
                }
                doc.add(new Paragraph("\n"));
                
                //Main Text
                //Parts
                for (int t = 0; t<legald.getParts().size(); t++) {
                    //String[] chap_letters = {"�","�","�","�","�","��","�","�","�","�","��","��","��","��","��","���","��","��","��"};

                    String par = "";
                    Paragraph paragraph3 = new Paragraph();
                    paragraph3.setFont(fontText);

                    //Article id and title
                    //ID
                    String par2 = part_header+" " + chap_letters[legald.getParts().get(t).getId()-1] + "\n";
                    Paragraph article = new Paragraph(par2, fontArticle);
                    article.setAlignment(Element.ALIGN_CENTER);
                    doc.add(article);

                    //Title
                    if (legald.getParts().get(t).getTitle() != null) {
                        String par3 = legald.getParts().get(t).getTitle() + "\n";
                        Paragraph articlet = new Paragraph(par3, fontArticle);
                        articlet.setAlignment(Element.ALIGN_CENTER);
                        doc.add(articlet);
                    }

                    Paragraph space = new Paragraph("\n", fontArticle);
                    article.setAlignment(Element.ALIGN_CENTER);
                    doc.add(space);
                if(!legald.getParts().get(t).getChapters().isEmpty()){
                //Chapters
                for (int w = 0; w<legald.getParts().get(t).getChapters().size(); w++) {

                    par = "";
                    paragraph3 = new Paragraph();
                    paragraph3.setFont(fontText);

                    //Article id and title
                    //ID
                    par2 = chap_header+" " + chap_letters[legald.getParts().get(t).getChapters().get(w).getId()-1] + "\n";
                    article = new Paragraph(par2, fontArticle);
                    article.setAlignment(Element.ALIGN_CENTER);
                    doc.add(article);

                    //Title
                    if (legald.getParts().get(t).getChapters().get(w).getTitle() != null) {
                        String par3 = legald.getParts().get(t).getChapters().get(w).getTitle() + "\n";
                        Paragraph articlet = new Paragraph(par3, fontArticle);
                        articlet.setAlignment(Element.ALIGN_CENTER);
                        doc.add(articlet);
                    }

                    space = new Paragraph("\n", fontArticle);
                    article.setAlignment(Element.ALIGN_CENTER);
                    doc.add(space);
                    //Articles
                    for (int i = 0; i<legald.getParts().get(t).getChapters().get(w).getArticles().size(); i++) {

                        //String[] letter = {"�","�","�","�","�","��","�","�","�","�","��","��","��","��","��","���","��","��","��","�","��","��","��","��","��","���","��","��","��","�","��","��","��","��","��","���","��","��","��","�"};

                        par = "";
                        paragraph3 = new Paragraph();
                        paragraph3.setFont(fontText);

                        //Article id and title
                        //ID
                        par2 = article_header+" " + legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getId() + "\n";
                        article = new Paragraph(par2, fontArticle);
                        article.setAlignment(Element.ALIGN_CENTER);
                        doc.add(article);

                        //Title
                        if (legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getTitle() != null) {
                            String par3 = legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getTitle() + "\n";
                            Paragraph articlet = new Paragraph(par3, fontArticle);
                            articlet.setAlignment(Element.ALIGN_CENTER);
                            doc.add(articlet);
                        }

                        space = new Paragraph("\n", fontArticle);
                        article.setAlignment(Element.ALIGN_CENTER);
                        doc.add(space);

                        //For all Paragraphs
                        for (int j = 0; j<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().size(); j++) {

                            //get Paragraph id
                            paragraph3.add("" + legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getId() + ". ");

                            //get Passages
                            for (int k = 0; k<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {

                                String a = legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                                a = trimDoubleQuotes(a);
                                paragraph3.add(a);

                                //If passage has modifications
                                if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification() != null) {

                                    paragraph3.add("\n\n"+quotes[0]);

                                    //if Modification type = Paraghraph
                                    if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Article")) {

                                        com.di.nomothesia.model.Article moda = (com.di.nomothesia.model.Article) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        for (int z = 0; z<moda.getParagraphs().size(); z++) {
                                            //get Modification's text
                                            for (int n = 0; n<moda.getParagraphs().get(z).getPassages().size(); n++) {
                                                paragraph3.add(moda.getParagraphs().get(z).getPassages().get(n).getText());
                                            }

                                            //get Modification Case
                                            for (int n = 0; n< moda.getParagraphs().get(z).getCaseList().size(); n++) {

                                                paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getId()+"");

                                                //get MOdification Case Passage text
                                                for (int l = 0; l<moda.getParagraphs().get(z).getCaseList().get(n).getPassages().size(); l++) {
                                                    paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getPassages().get(l).getText());
                                                }

                                            }

                                            paragraph3.add("\n");
                                        }

                                    }
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Paragraph")) {

                                        com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //get Modification's text
                                        for (int n = 0; n<p.getPassages().size(); n++) {
                                            paragraph3.add(p.getPassages().get(n).getText());
                                        }

                                        //get Modification Case
                                        for (int n = 0; n< p.getCaseList().size(); n++) {

                                            paragraph3.add(p.getCaseList().get(n).getId()+"");

                                            //get MOdification Case Passage text
                                            for (int l = 0; l<p.getCaseList().get(n).getPassages().size(); l++) {
                                                paragraph3.add(p.getCaseList().get(n).getPassages().get(l).getText());
                                            }

                                        }

                                        paragraph3.add("\n");

                                    }
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Passage")) {

                                        Passage p = (Passage) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        paragraph3.add(p.getText());
                                    }
                                    //If Modification type = Case
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Case")) {

                                        //get Fragment
                                        Case c = (Case) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //Get Modification Passage text
                                        for (int l = 0; l<c.getPassages().size(); l++) {
                                            paragraph3.add(c.getPassages().get(l).getText());
                                        }

                                    }

                                    paragraph3.add(quotes[1]+".\n\n");

                                }

                            }

                            paragraph3.add("\n");

                            //get Cases
                            for (int k = 0; k<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {

                                //Case id
                                paragraph3.setTabSettings(new TabSettings(16f));
                                paragraph3.add(Chunk.TABBING);
                                paragraph3.add(new Chunk(letter[k]+". "));

                                //get Case Passage
                                for (int l = 0; l<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {

                                    String b = legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                                    b = trimDoubleQuotes(b);
                                    paragraph3.add(b);

                                    paragraph3.add("\n");

                                }

                                //Case in case
                                if (legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList() != null) {

                                    for (int p = 0; p<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().size(); p++) {

                                        //Case id
                                        paragraph3.setTabSettings(new TabSettings(16f));
                                        paragraph3.add(Chunk.TABBING);
                                        paragraph3.add(Chunk.TABBING);
                                        paragraph3.add(new Chunk(letter[p]+letter[p]+". "));

                                        //get Case Passage
                                        for (int l = 0; l<legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().size(); l++) {

                                            String c = legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().get(l).getText();
                                            c = trimDoubleQuotes(c);
                                            paragraph3.add(c);
                                            paragraph3.add("\n");

                                        }

                                    }
                                }

                                //If passage has modifications
                                if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {

                                    paragraph3.add("\n\n"+quotes[0]);

                                    //if Modification type = Paraghraph
                                    if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")) {

                                        com.di.nomothesia.model.Article moda = (com.di.nomothesia.model.Article) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();

                                        for (int z = 0; z<moda.getParagraphs().size(); z++) {
                                            //get Modification's text
                                            for (int n = 0; n<moda.getParagraphs().get(z).getPassages().size(); n++) {
                                                paragraph3.add(moda.getParagraphs().get(z).getPassages().get(n).getText());
                                            }

                                            //get Modification Case
                                            for (int n = 0; n< moda.getParagraphs().get(z).getCaseList().size(); n++) {

                                                paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getId()+"");

                                                //get MOdification Case Passage text
                                                for (int l = 0; l<moda.getParagraphs().get(z).getCaseList().get(n).getPassages().size(); l++) {
                                                    paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getPassages().get(l).getText());
                                                }

                                            }

                                            paragraph3.add("\n");
                                        }

                                    }
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")) {

                                        com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //get Modification's text
                                        for (int n = 0; n<p.getPassages().size(); n++) {
                                            paragraph3.add(p.getPassages().get(n).getText());
                                        }

                                        //get Modification Case
                                        for (int n = 0; n< p.getCaseList().size(); n++) {

                                            paragraph3.add(p.getCaseList().get(n).getId()+"");

                                            //get MOdification Case Passage text
                                            for (int l = 0; l<p.getCaseList().get(n).getPassages().size(); l++) {
                                                paragraph3.add(p.getCaseList().get(n).getPassages().get(l).getText());
                                            }

                                        }

                                        paragraph3.add("\n");

                                    } 
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Passage")) {

                                        Passage p = (Passage) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();

                                        paragraph3.add(p.getText());
                                    }
                                    //If Modification type = Case
                                    else if(legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Case")) {
                                        
//                                        //get Fragment
//                                        Case c = (Case) legald.getParts().get(t).getChapters().get(w).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();
//
//                                        //Get Modification Passage text
//                                        for (int l = 0; l<c.getPassages().size(); l++) {
//                                            paragraph3.add(c.getPassages().get(l).getText());
//                                        }

                                    }

                                    paragraph3.add(quotes[1]+"\n\n");

                                }

                            }



                            paragraph3.add("\n");

                        }

                            paragraph3.add("\n");
                            doc.add(paragraph3);
                    }
                }
                }
                else{
                    //Articles
                    for (int i = 0; i<legald.getParts().get(t).getArticles().size(); i++) {

                        //String[] letter = {"�","�","�","�","�","��","�","�","�","�","��","��","��","��","��","���","��","��","��","�","��","��","��","��","��","���","��","��","��","�","��","��","��","��","��","���","��","��","��","�"};

                        par = "";
                        paragraph3 = new Paragraph();
                        paragraph3.setFont(fontText);

                        //Article id and title
                        //ID
                        par2 = article_header+" " + legald.getParts().get(t).getArticles().get(i).getId() + "\n";
                        article = new Paragraph(par2, fontArticle);
                        article.setAlignment(Element.ALIGN_CENTER);
                        doc.add(article);

                        //Title
                        if (legald.getParts().get(t).getArticles().get(i).getTitle() != null) {
                            String par3 = legald.getParts().get(t).getArticles().get(i).getTitle() + "\n";
                            Paragraph articlet = new Paragraph(par3, fontArticle);
                            articlet.setAlignment(Element.ALIGN_CENTER);
                            doc.add(articlet);
                        }

                        space = new Paragraph("\n", fontArticle);
                        article.setAlignment(Element.ALIGN_CENTER);
                        doc.add(space);

                        //For all Paragraphs
                        for (int j = 0; j<legald.getParts().get(t).getArticles().get(i).getParagraphs().size(); j++) {

                            //get Paragraph id
                            paragraph3.add("" + legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getId() + ". ");

                            //get Passages
                            for (int k = 0; k<legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {

                                String a = legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                                a = trimDoubleQuotes(a);
                                paragraph3.add(a);

                                //If passage has modifications
                                if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification() != null) {

                                    paragraph3.add("\n\n"+quotes[0]);

                                    //if Modification type = Paraghraph
                                    if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Article")) {

                                        com.di.nomothesia.model.Article moda = (com.di.nomothesia.model.Article) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        for (int z = 0; z<moda.getParagraphs().size(); z++) {
                                            //get Modification's text
                                            for (int n = 0; n<moda.getParagraphs().get(z).getPassages().size(); n++) {
                                                paragraph3.add(moda.getParagraphs().get(z).getPassages().get(n).getText());
                                            }

                                            //get Modification Case
                                            for (int n = 0; n< moda.getParagraphs().get(z).getCaseList().size(); n++) {

                                                paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getId()+"");

                                                //get MOdification Case Passage text
                                                for (int l = 0; l<moda.getParagraphs().get(z).getCaseList().get(n).getPassages().size(); l++) {
                                                    paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getPassages().get(l).getText());
                                                }

                                            }

                                            paragraph3.add("\n");
                                        }

                                    }
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Paragraph")) {

                                        com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //get Modification's text
                                        for (int n = 0; n<p.getPassages().size(); n++) {
                                            paragraph3.add(p.getPassages().get(n).getText());
                                        }

                                        //get Modification Case
                                        for (int n = 0; n< p.getCaseList().size(); n++) {

                                            paragraph3.add(p.getCaseList().get(n).getId()+"");

                                            //get MOdification Case Passage text
                                            for (int l = 0; l<p.getCaseList().get(n).getPassages().size(); l++) {
                                                paragraph3.add(p.getCaseList().get(n).getPassages().get(l).getText());
                                            }

                                        }

                                        paragraph3.add("\n");

                                    }
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Passage")) {

                                        Passage p = (Passage) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        paragraph3.add(p.getText());
                                    }
                                    //If Modification type = Case
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getType().equals("Case")) {

                                        //get Fragment
                                        Case c = (Case) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //Get Modification Passage text
                                        for (int l = 0; l<c.getPassages().size(); l++) {
                                            paragraph3.add(c.getPassages().get(l).getText());
                                        }

                                    }

                                    paragraph3.add(quotes[1]+".\n");

                                }

                            }

                            paragraph3.add("\n");

                            //get Cases
                            for (int k = 0; k<legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {

                                //Case id
                                paragraph3.setTabSettings(new TabSettings(16f));
                                paragraph3.add(Chunk.TABBING);
                                paragraph3.add(new Chunk(letter[k]+". "));

                                //get Case Passage
                                for (int l = 0; l<legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {

                                    String b = legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                                    b = trimDoubleQuotes(b);
                                    paragraph3.add(b);

                                    paragraph3.add("\n");

                                }

                                //Case in case
                                if (legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList() != null) {

                                    for (int p = 0; p<legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().size(); p++) {

                                        //Case id
                                        paragraph3.setTabSettings(new TabSettings(16f));
                                        paragraph3.add(Chunk.TABBING);
                                        paragraph3.add(Chunk.TABBING);
                                        paragraph3.add(new Chunk(letter[p]+letter[p]+". "));

                                        //get Case Passage
                                        for (int l = 0; l<legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().size(); l++) {

                                            String c = legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().get(l).getText();
                                            c = trimDoubleQuotes(c);
                                            paragraph3.add(c);
                                            paragraph3.add("\n");

                                        }

                                    }
                                }

                                //If passage has modifications
                                if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification() != null) {

                                    paragraph3.add(quotes[0]);

                                    //if Modification type = Paraghraph
                                    if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Article")) {

                                        com.di.nomothesia.model.Article moda = (com.di.nomothesia.model.Article) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();

                                        for (int z = 0; z<moda.getParagraphs().size(); z++) {
                                            //get Modification's text
                                            for (int n = 0; n<moda.getParagraphs().get(z).getPassages().size(); n++) {
                                                paragraph3.add(moda.getParagraphs().get(z).getPassages().get(n).getText());
                                            }

                                            //get Modification Case
                                            for (int n = 0; n< moda.getParagraphs().get(z).getCaseList().size(); n++) {

                                                paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getId()+"");

                                                //get MOdification Case Passage text
                                                for (int l = 0; l<moda.getParagraphs().get(z).getCaseList().get(n).getPassages().size(); l++) {
                                                    paragraph3.add(moda.getParagraphs().get(z).getCaseList().get(n).getPassages().get(l).getText());
                                                }

                                            }

                                            paragraph3.add("\n");
                                        }

                                    }
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Paragraph")) {

                                        com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();

                                        //get Modification's text
                                        for (int n = 0; n<p.getPassages().size(); n++) {
                                            paragraph3.add(p.getPassages().get(n).getText());
                                        }

                                        //get Modification Case
                                        for (int n = 0; n< p.getCaseList().size(); n++) {

                                            paragraph3.add(p.getCaseList().get(n).getId()+"");

                                            //get MOdification Case Passage text
                                            for (int l = 0; l<p.getCaseList().get(n).getPassages().size(); l++) {
                                                paragraph3.add(p.getCaseList().get(n).getPassages().get(l).getText());
                                            }

                                        }

                                        paragraph3.add("\n");

                                    } 
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Passage")) {

                                        Passage p = (Passage) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getFragment();

                                        paragraph3.add(p.getText());
                                    }
                                    //If Modification type = Case
                                    else if(legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getModification().getType().equals("Case")) {
                                        
//                                        //get Fragment
//                                        Case c = (Case) legald.getParts().get(t).getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getModification().getFragment();
//
//                                        //Get Modification Passage text
//                                        for (int l = 0; l<c.getPassages().size(); l++) {
//                                            paragraph3.add(c.getPassages().get(l).getText());
//                                        }

                                    }

                                    paragraph3.add(quotes[1]+"\n");

                                }

                            }



                            paragraph3.add("\n");

                        }

                            paragraph3.add("\n");
                            doc.add(paragraph3);

                    }
                    
                }            
                //Date
                String date = legald.getPublicationDate();
                date = trimDoubleQuotes(date);
                String finaldate = "\n\n"+city+", " + date;
                Paragraph dat = new Paragraph(finaldate, fontDate);
                dat.setAlignment(Element.ALIGN_CENTER);
                doc.add(dat);
                doc.add(new Paragraph("\n"));
                
                //Signers Full
                for (int i = 0; i<legald.getSigners().size(); i++) {
                    String signerfull = "";
//                    signerfull += "� ";
//                    signerfull += legald.getSigners().get(i).getTitle();
//                    //signerfull = trimDoubleQuotes(signer);
//                    signerfull += "\n";
                    signerfull += legald.getSigners().get(i).getFullName();
                    //signerfull = trimDoubleQuotes(signerfull);
                    signerfull += "\n\n\n";
                    Paragraph signfull = new Paragraph(signerfull, fontSignerf);
                    signfull.setAlignment(Element.ALIGN_CENTER);
                    doc.add(signfull);
                }
                }
        
	}
        
        public static String trimDoubleQuotes(String text) {
            int textLength = text.length();
            if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
                return text.substring(1, textLength - 1);
            }
            return text;
        }

}

