/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.controller;

import com.di.nomothesia.model.Article;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Passage;
import com.di.nomothesia.model.Signer;
import com.di.nomothesia.service.AbstractITextPdfView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Panagiotis
 */
public class PDFBuilder extends AbstractITextPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container
		LegalDocument legald = (LegalDocument) model.get("legaldocument");
                BaseFont bf = BaseFont.createFont("c:/windows/fonts/times.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                bf.setSubset(true);
                Font font = new Font(bf, 24);
                Font font2 = new Font(bf, 12);
                font.setColor(BaseColor.BLACK);
                
                doc.setMargins(36, 36, 108, 180);
                Paragraph paragraph = new Paragraph("≈ÀÀ«Õ… « ƒ«Ãœ —¡‘…¡", font);
                paragraph.setAlignment(Element.ALIGN_CENTER);
		doc.add(paragraph);
                
                doc.add(new Paragraph(legald.getTitle(),font));
                doc.add(new Paragraph(legald.getPublicationDate(),font));
                for (int i = 0; i<legald.getArticles().size(); i++) {
                    String par = "¢ÒËÒÔ " + legald.getArticles().get(i).getId() + "\n\n";

                    for (int j = 0; j<legald.getArticles().get(i).getParagraphs().size(); j++) {
                        par += "" + legald.getArticles().get(i).getParagraphs().get(j).getId() + ". ";
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
                            String a = legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
                            a = trimDoubleQuotes(a);
                            par += a;
                            
                        }
                        par += "\n";
                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
                            par += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();
                            for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
                                String b = legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
                                b = trimDoubleQuotes(b);
                                par += b;
                                par += "\n";
                            }
                        }
                        par += "\n\n";
                        Paragraph paragraph2 = new Paragraph(par,font2);
                        paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
                        doc.add(paragraph2);
                        par ="";
                    }
                    
                }
		
		//PdfPTable table = new PdfPTable(5);
		//table.setWidthPercentage(100.0f);
		//table.setWidths(new float[] {3.0f, 2.0f, 2.0f, 2.0f, 1.0f});
		//table.setSpacingBefore(10);
		
		// define font for table header row
                //BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf","cp1253", BaseFont.EMBEDDED);
                //Font font = new Font(bf, 12); 
                //BaseFont bfTimes = BaseFont.createFont(FontFactory.COURIER_BOLD,"CP1253", BaseFont.EMBEDDED);
                //Font font = new Font(bfTimes, 12, Font.BOLD); 
		//Font font = FontFactory.getFont(FontFactory.TIMES);
		//font.setColor(BaseColor.WHITE);
		
		// define table header cell
//		PdfPCell cell = new PdfPCell();
//		cell.setBackgroundColor(BaseColor.BLUE);
//		cell.setPadding(5);
//		
//		// write table header 
//		cell.setPhrase(new Phrase("Book Title ÂﬂÌ·È ¯ÛÛ·", font));
//		table.addCell(cell);
//		
//		cell.setPhrase(new Phrase("Author", font));
//		table.addCell(cell);
//
//		cell.setPhrase(new Phrase("ISBN", font));
//		table.addCell(cell);
//		
//		cell.setPhrase(new Phrase("Published Date", font));
//		table.addCell(cell);
//		
//		cell.setPhrase(new Phrase("Price", font));
//		table.addCell(cell);
//		
//		// write table row data
//		for (Book aBook : listBooks) {
//			table.addCell(aBook.getTitle());
//			table.addCell(aBook.getAuthor());
//			table.addCell(aBook.getIsbn());
//			table.addCell(aBook.getPublishedDate());
//			table.addCell(String.valueOf(aBook.getPrice()));
//		}
		
		//doc.add(table);
		
	}
        
        public static String trimDoubleQuotes(String text) {
            int textLength = text.length();
            if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
                return text.substring(1, textLength - 1);
            }
            return text;
        }

}
