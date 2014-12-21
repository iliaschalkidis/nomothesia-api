package com.di.nomothesia.controller;

import com.di.nomothesia.dao.LegalDocumentDAO;
import com.di.nomothesia.dao.LegalDocumentDAOImpl;
import com.di.nomothesia.model.LegalDocument;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	@RequestMapping(value = "/legislation/{type}/{year}/{id}", method = RequestMethod.GET)
	public String presentLegalDocument(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegalDocumentDAO ld = new LegalDocumentDAOImpl();
                LegalDocument legaldoc = ld.getById(type, year, id);
                model.addAttribute("legaldoc", legaldoc);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{typeoflegislation}/{year}/{id}/data.xml", method = RequestMethod.GET)
	public String exportToXML(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/{typeoflegislation}/{year}/{id}/data.rdf", method = RequestMethod.GET)
	public String exportToRDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/search", method = RequestMethod.GET)
	public String search(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/endpoint", method = RequestMethod.GET)
	public String endpoint(@RequestParam Map<String,String> params, Model model) {
		if(params.get("query") != null){
                    LegalDocumentDAO ld = new LegalDocumentDAOImpl();
                    String results = ld.sparqlQuery(params.get("query"));
                    model.addAttribute("results", results);
                }
		return "endpoint";
	}
        
        /**
	 * Handle request to download a PDF document 
	 */
	@RequestMapping(value = "/legislation/{type}/{year}/{id}/data.pdf", method = RequestMethod.GET)
	public ModelAndView exportToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegalDocument legal = new LegalDocument();

                LegalDocumentDAO ld = new LegalDocumentDAOImpl();
                legal = ld.getById(type,year,id);
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
	
}
