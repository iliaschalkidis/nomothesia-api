package com.di.nomothesia.controller;

import com.di.nomothesia.model.LegalDocument;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LegislationController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/legislation/{typeoflegislation}/{year}/{id}", method = RequestMethod.GET)
	public String presentLegalDocument(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/{typeoflegislation}/{year}/{id}/data.xml", method = RequestMethod.GET)
	public String presentXML(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/search", method = RequestMethod.GET)
	public String search(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return "home";
	}
        
        /**
	 * Handle request to download a PDF document 
	 */
	@RequestMapping(value = "/downloadPDF", method = RequestMethod.GET)
	public ModelAndView downloadExcel() {
		// create some sample data
                LegalDocument legal = new LegalDocument();
//		List<Book> listBooks = new ArrayList<Book>();
//		listBooks.add(new Book("Spring in Action είναι μαλάκας", "Craig Walls", "1935182358",
//				"June 29th 2011", 31.98F));
//		listBooks.add(new Book("Spring in Practice", "Willie Wheeler, Joshua White",
//				"1935182056", "May 16th 2013", 31.95F));
//		listBooks.add(new Book("Pro Spring 3",
//				"Clarence Ho, Rob Harrop", "1430241071", "April 18th 2012", 31.85F));
//		listBooks.add(new Book("Spring Integration in Action", "Mark Fisher", "1935182439",
//				"September 26th 2012", 28.73F));

		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
	
}
