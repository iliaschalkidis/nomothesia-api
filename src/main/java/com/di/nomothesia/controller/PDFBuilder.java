/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.controller;

import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.AbstractITextPdfView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
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
		//LegalDocument legald = (LegalDocument) model.get("legaldocument");
                BaseFont bf = BaseFont.createFont("c:/windows/fonts/times.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                bf.setSubset(true);
                Font font = new Font(bf, 24);
                font.setColor(BaseColor.BLACK);
                
		LegalDocument legald = new LegalDocument();
                Paragraph paragraph = new Paragraph("≈ÀÀ«Õ… « ƒ«Ãœ —¡‘…¡", font);
                paragraph.setAlignment(Element.ALIGN_CENTER);
		doc.add(paragraph);
                
//                for (int i = 0; i<legald.getArticles().size(); i++) {
//                    
//                    for (int j = 0; j<legald.getArticles().get(i).getParagraphs().size(); j++) {
//                        
//                        String paragraph = "" + legald.getArticles().get(i).getParagraphs().get(j).getId();
//                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size(); k++) {
//                            paragraph += legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText();
//                        }
//                        
//                        for (int k = 0; k<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size(); k++) {
//                            paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getId();
//                            for (int l = 0; l<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().size(); l++) {
//                                paragraph += legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(k).getPassages().get(l).getText();
//                            }
//                        }
//                        doc.add(new Paragraph(paragraph));
//                    }
//                    
//                }
		
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

}
