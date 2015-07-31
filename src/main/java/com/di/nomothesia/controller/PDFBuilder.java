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
import com.itextpdf.text.TabSettings;
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
                doc.add(new Paragraph("\n\n\n\n\n\n\n"));
                
                //Fonts
                BaseFont bf = BaseFont.createFont(getServletContext().getRealPath("/resources/fonts/arial.ttf"),BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                bf.setSubset(true);
                Font fontTitle = new Font(bf, 18,Font.BOLD);
                Font fontText = new Font(bf, 12);
                Font citationText = new Font(bf, 10);
                Font fontArticle = new Font(bf, 12,Font.BOLD);
                Font fontDate = new Font(bf, 12);
                Font fontType = new Font(bf, 20,Font.BOLD);
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
                
                //Citations
                String citation = "Έχοντας υπόψη: \n\n";
                Paragraph cit1 = new Paragraph(citation, fontText);
                cit1.setAlignment(Element.ALIGN_LEFT);
                String citation2 = "";
                
                for (int i=0; i<legald.getCitations().size();i++){
                    citation2 += legald.getCitations().get(i).getId() + ". " + legald.getCitations().get(i).getDescription() + "\n\n";
                }
                
                Paragraph cit2 = new Paragraph(citation2, citationText);
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
                    
                    String[] letter = {"α","β","γ","δ","ε","στ","ζ","η","θ","ι","ια","ιβ","ιγ","ιδ","ιε","ιστ","ιζ","ιη","ιθ"};
        
                    String par = "";
                    Paragraph paragraph3 = new Paragraph();
                    paragraph3.setFont(fontText);
                    
                    //Article id and title
                    //ID
                    String par2 = "'Αρθρο " + legald.getArticles().get(i).getId() + "\n";
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
                        paragraph3.add("" + legald.getArticles().get(i).getParagraphs().get(j).getId() + ". ");
                        
                        //get Passages
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
                            
                            String a = legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                            a = trimDoubleQuotes(a);
                            paragraph3.add(a);
                            
                        }
                        
                        paragraph3.add("\n");
                        
                        //get Cases
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
                            
                            //Case id
                            paragraph3.setTabSettings(new TabSettings(16f));
                            paragraph3.add(Chunk.TABBING);
                            paragraph3.add(new Chunk(letter[k]+". "));
                            
                            //get Case Passage
                            for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
                           
                                String b = legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                                b = trimDoubleQuotes(b);
                                paragraph3.add(b);
                                
                                paragraph3.add("\n");
                            
                            }
                            
                            //Case in case
                            if (legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList() != null) {
                                                                
                                for (int p = 0; p<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().size(); p++) {

                                    //Case id
                                    paragraph3.setTabSettings(new TabSettings(16f));
                                    paragraph3.add(Chunk.TABBING);
                                    paragraph3.add(Chunk.TABBING);
                                    paragraph3.add(new Chunk(letter[p]+letter[p]+". "));

                                    //get Case Passage
                                    for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().size(); l++) {

                                        String c = legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getCaseList().get(p).getPassages().get(l).getText();
                                        c = trimDoubleQuotes(c);
                                        paragraph3.add(c);
                                        paragraph3.add("\n");

                                    }

                                }
                            }
                        
                        }
                                                
                        //If document has modifications
                        if(legald.getArticles().get(i).getParagraphs().get(j).getModification() != null) {
                            
                            paragraph3.add("\"");
                            
                            //if Modification type = Paraghraph
                            if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Paragraph")) {
                        
                                com.di.nomothesia.model.Paragraph p = (com.di.nomothesia.model.Paragraph) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                                
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
                    
                            } //If Modification type = Case
                            else if(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType().equals("Case")) {
                                
                                //get Fragment
                                Case c = (Case) legald.getArticles().get(i).getParagraphs().get(j).getModification().getFragment();
                                
                                //Get Modification Passage text
                                for (int l = 0; l<c.getPassages().size(); l++) {
                                    paragraph3.add(c.getPassages().get(l).getText());
                                }
                                
                            }
                            
                            paragraph3.add("\"\n");
                
                        }
                        
                        paragraph3.add("\n");
                      
                    }
                    
                        paragraph3.setAlignment(Element.ALIGN_JUSTIFIED);
                        doc.add(paragraph3);
                        
                }
		
                doc.newPage();
                
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
