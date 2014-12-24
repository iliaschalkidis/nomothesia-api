package com.di.nomothesia.controller;

import com.di.nomothesia.model.EndpointResult;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.LegislationService;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id);
                model.addAttribute("legaldoc", legaldoc);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{type1}/{id1}/{type2}/{id2}", method = RequestMethod.GET)
	public String presentLegalFragment(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String type1, @PathVariable String id1, @PathVariable String type2, @PathVariable String id2, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("type1", type1);
                model.addAttribute("id1", id1);
                model.addAttribute("type2", type2);
                model.addAttribute("id2", id2);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.xml", method = RequestMethod.GET, produces={"application/xml"})
        public ResponseEntity<String> exportToXML(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws TransformerException{
            LegislationService lds = new LegislationService();
            String xml = lds.getXMLById(type,year,id);
            
            return new ResponseEntity<String>(xml,new HttpHeaders(),HttpStatus.CREATED);
}
        
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.rdf", method = RequestMethod.GET,  produces={"application/xml"})
	public ResponseEntity<String> exportToRDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws JAXBException {
            LegislationService lds = new LegislationService();
            String rdfResult = lds.getRDFById(type,year,id);
            
            return new ResponseEntity<String>(rdfResult,new HttpHeaders(),HttpStatus.CREATED);
	
        }
        
        @RequestMapping(value = "/legislation/search", method = RequestMethod.GET)
	public String search(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return "home";
	}
        
        @RequestMapping(value = "/legislation/endpoint", method = RequestMethod.GET)
	public String endpoint(@RequestParam Map<String,String> params, Model model) {
		if(params.get("query") != null){
                    LegislationService lds = new LegislationService();
                    EndpointResult eprs = lds.sparqlQuery(params.get("query"));
                    model.addAttribute("endpointResults", eprs);
                }
		return "endpoint";
	}
        
        @RequestMapping(value = "/legislation/endpoint/query/{id}", method = RequestMethod.GET)
	public String endpoint( @PathVariable String id, Model model) {
		if(id != null){
                    LegislationService lds = new LegislationService();
                    EndpointResult eprs = lds.sparqlQuery(id);
                    model.addAttribute("endpointResults", eprs);
                }
		return "endpoint";
	}
        
        /**
	 * Handle request to download a PDF document 
	 */
	@RequestMapping(value = "/legislation/{type}/{year}/{id}/data.pdf", method = RequestMethod.GET)
	public ModelAndView exportToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
                LegalDocument legal = lds.getById(type,year,id);
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
	
}
