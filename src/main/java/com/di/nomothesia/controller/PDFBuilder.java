
package com.di.nomothesia.controller;

import com.di.nomothesia.model.Case;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.AbstractITextPdfView;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PDFBuilder extends AbstractITextPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
                // Get data model which is passed by the Controller
		LegalDocument legald = (LegalDocument) model.get("legaldocument");
                
                //Doc margins
                //doc.setMargins(60, 60, 20, 20);
                
                //Add image on top
                String relativeWebPath = "/resources/images/banner.png";
                String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
                Image img = Image.getInstance(absoluteDiskPath);
                img.scalePercent(65f);
                img.setAlignment(Element.ALIGN_CENTER);
                doc.add(img);
                
                //Add line seperator
                LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -45);
                doc.add(new Chunk(line));
                doc.add(new Paragraph("\n\n\n\n\n\n\n\n"));
                
                //Fonts
                BaseFont bf = BaseFont.createFont("c:/windows/fonts/tahoma.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                bf.setSubset(true);
                Font fontTitle = new Font(bf, 18,Font.BOLD);
                Font fontText = new Font(bf, 12);
                Font fontArticle = new Font(bf, 12,Font.BOLD);
                Font fontDate = new Font(bf, 12);
                Font fontType = new Font(bf, 20,Font.BOLD);
                //Font fontSigner = new Font(bf, 13);
                Font fontSignerf = new Font(bf, 11);
                
                //Type
                String tid = legald.getId();
                tid = trimDoubleQuotes(tid);
                String type = legald.getDecisionType();
                type = trimDoubleQuotes(type);
                type += " ΥΠ' ΑΡΙΘ. " + tid;
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
                
                //Writers
//                doc.add(new Paragraph("\n"));
//                String signer = "";
//                if (legald.getSigners().size()<2) {
//                    signer += "Ο ";
//                }
//                else {
//                    signer += "ΟΙ ";
//                }
//                for (int i = 0; i<legald.getSigners().size(); i++) {
//                    if (i == legald.getSigners().size()-1) {
//                        signer += legald.getSigners().get(i).getTitle();
//                        //signer = trimDoubleQuotes(signer);
//                    }
//                    else {
//                        signer += legald.getSigners().get(i).getTitle();
//                        //signer = trimDoubleQuotes(signer);
//                        signer += " /";
//                    }
//                }
//                Paragraph sign = new Paragraph(signer, fontSigner);
//                sign.setAlignment(Element.ALIGN_CENTER);
//                doc.add(sign);
//                doc.add(new Paragraph("\n"));
                
                //Citation
                String citation = "Έχοντας υπόψη: \n\n";
                Paragraph cit1 = new Paragraph(citation, fontText);
                cit1.setAlignment(Element.ALIGN_LEFT);
                String citation2 = "1. Τις διατάξεις:\n" +
                    " \t α. Της παρ. 1 του άρθρου 5 του Ν. 679/1977, «Περί "+
                    "αυξήσεως θέσεων προσωπικού του Υπουργείου Δημο" +
                    "σίων Έργων και ρυθμίσεως συναφών θεμάτων» (Α΄ 245) "+
                    "όπως τα δύο τελευταία εδάφια της παραγράφου αυτής " +
                    "τροποποιήθηκαν και συμπληρώθηκαν με την παρ. 1, του " +
                    "άρθρου 23 του Ν. 1418/1984 (Α΄ 23).\n" +
                    " \t β. Του άρθρου 8 του Ν. 679/1977 (Α΄ 245), όπως αυτές " +
                    "τροποποιήθηκαν και συμπληρώθηκαν με τις παραγρά" +
                    "φους 2 και 3 του άρθρου 5 του Ν. 2229/1994 (Α΄ 138), " +
                    "σύμφωνα με τις οποίες θα καλυφθεί η προκαλούμενη " +
                    "από το διάταγμα δαπάνη.\n\n";
                Paragraph cit2 = new Paragraph(citation2, fontText);
                cit2.setAlignment(Element.ALIGN_JUSTIFIED);
                String citation3 = "Αποφασίζουμε: \n";
                Paragraph cit3 = new Paragraph(citation3, fontText);
                cit3.setAlignment(Element.ALIGN_CENTER);
                doc.add(cit1);
                doc.add(cit2);
                doc.add(cit3);
                doc.add(new Paragraph("\n"));
                
                //Main Text
                //Articles
                for (int i = 0; i<legald.getArticles().size(); i++) {
                    
                    char letter = 'α';
                    String par = "";
                    
                    //Article id and title
                    //ID
                    String par2 = "Άρθρο " + legald.getArticles().get(i).getId() + "\n";
                    Paragraph article = new Paragraph(par2, fontArticle);
                    article.setAlignment(Element.ALIGN_CENTER);
                    doc.add(article);
                    
                    //Title
                    if (legald.getArticles().get(i).getTitle() != null) {
                        String par3 = legald.getArticles().get(i).getTitle() + "\n";
                        Paragraph articlet = new Paragraph(par3, fontArticle);
                        articlet.setAlignment(Element.ALIGN_CENTER);
                        doc.add(articlet);
                    }
                    
                    //For all Paragraphs
                    for (int j = 0; j<legald.getArticles().get(i).getParagraphs().size(); j++) {
                        
                        //get Paragraph id
                        par += "" + legald.getArticles().get(i).getParagraphs().get(j).getId() + ". ";
                        
                        //get Passages
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
                            
                            String a = legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                            a = trimDoubleQuotes(a);
                            par += a;
                            
                        }
                        
                        par += "\n";
                        
                        //get Cases
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
                            
                            //Case id
                            par += "   " + letter+". ";
                            letter++;
                            //par += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();
                            
                            //get Case Passage
                            for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
                               
                                String b = legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                                b = trimDoubleQuotes(b);
                                par += b;
                                par += "\n";
                            
                            }
                            
                            //Case in case
                            if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList() != null) {
                                
                                char letter2 = 'α';
                                
                                for (int p = 0; p<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().size(); p++) {

                                    //Case id
                                    par += "      " + letter2+". ";
                                    letter2++;
                                    //par += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();

                                    //get Case Passage
                                    for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().size(); l++) {

                                        String c = legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().get(l).getText();
                                        c = trimDoubleQuotes(c);
                                        par += c;
                                        par += "\n";

                                    }

                                }
                            }
                        
                        }
                        
                        letter = 'α';
                        
                        //If document has modifications
                        if(legald.getArticles().get(i).getParagraphs().get(j).getModification() != null) {
                            
                            par += "\"";
                            
                            //if Modification type = Paraghraph
                            if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Paragraph")) {
                        
                                com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                                
                                //get Modification's text
                                for (int n = 0; n<p.getPassages().size(); n++) {
                                    par += p.getPassages().get(n).getText();
                                }
                                
                                //get Modification Case
                                for (int n = 0; n< p.getCaseList().size(); n++) {
                                    
                                    par += p.getCaseList().get(n).getId();
                                    
                                    //get MOdification Case Passage text
                                    for (int l = 0; l<p.getCaseList().get(n).getPassages().size(); l++) {
                                        par += p.getCaseList().get(n).getPassages().get(l).getText();
                                    }
                                
                                }
                        
                                //par +="\n";
                    
                            } //If Modification type = Case
                            else if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Case")) {
                                
                                //get Fragment
                                Case c = (Case) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                                
                                //Get Modification Passage text
                                for (int l = 0; l<c.getPassages().size(); l++) {
                                    par += c.getPassages().get(l).getText();
                                }
                            }
                            
                            par += "\"\n";
                
                        }
                        
                        par += "\n";
                        Paragraph paragraph2 = new Paragraph(par,fontText);
                        paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
                        doc.add(paragraph2);
                        par ="";
                        
                    
                    }
                    
                }
		
                //Date
                String date = legald.getPublicationDate();
                date = trimDoubleQuotes(date);
                String finaldate = "\n\n Αθήνα, " + date;
                Paragraph dat = new Paragraph(finaldate, fontDate);
                dat.setAlignment(Element.ALIGN_CENTER);
                doc.add(dat);
                doc.add(new Paragraph("\n"));
                
                //Signers Full
                for (int i = 0; i<legald.getSigners().size(); i++) {
                    String signerfull = "";
                    signerfull += "Ο ";
                    signerfull += legald.getSigners().get(i).getTitle();
                    //signerfull = trimDoubleQuotes(signer);
                    signerfull += "\n";
                    signerfull += legald.getSigners().get(i).getFullName();
                    //signerfull = trimDoubleQuotes(signerfull);
                    signerfull += "\n\n\n";
                    Paragraph signfull = new Paragraph(signerfull, fontSignerf);
                    signfull.setAlignment(Element.ALIGN_CENTER);
                    doc.add(signfull);
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
