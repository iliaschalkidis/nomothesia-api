package com.di.nomothesia.controller;

import com.di.nomothesia.model.EndpointResult;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.LegislationService;
import java.util.List;
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
	
	@RequestMapping(value = "/legislation/{type}/{year}/{id}/enacted", method = RequestMethod.GET)
	public String presentOriginalLegalDocument(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                model.addAttribute("legaldoc", legaldoc);
                List<LegalDocument> legalmods = lds.getAllModificationsById(type, year, id);
                model.addAttribute("legalmods", legalmods);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}", method = RequestMethod.GET)
	public String presentUpdatedLegalDocument(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getUpdatedById(type, year, id, 1, null);
                model.addAttribute("legaldoc", legaldoc);
                List<LegalDocument> legalmods = lds.getAllModificationsById(type, year, id);
                model.addAttribute("legalmods", legalmods);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{type1}/{id1}/{type2}/{id2}", method = RequestMethod.GET)
	public String presentLegalFragment(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String type1, @PathVariable String id1, @PathVariable String type2, @PathVariable String id2, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id , 1);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("type1", type1);
                model.addAttribute("id1", id1);
                model.addAttribute("type2", type2);
                model.addAttribute("id2", id2);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}", method = RequestMethod.GET)
        public String presentModificationByDate(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd, Model model) {
		LegislationService lds = new LegislationService();
                String date = "";
                date += yyyy + "-" + mm + "-" + dd;
                LegalDocument legaldoc = lds.getUpdatedById(type, year, id, 1, date);
                model.addAttribute("legaldoc", legaldoc);
                List<LegalDocument> legalmods = lds.getAllModificationsById(type, year, id);
                model.addAttribute("legalmods", legalmods);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}/data.xml", method = RequestMethod.GET, produces={"application/xml"})
        public ResponseEntity<String> exportDateToXML(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd) throws TransformerException{
            LegislationService lds = new LegislationService();
            String date = "";
            date += yyyy + "-" + mm + "-" + dd;
            String xml = lds.getUpdatedXMLByIdDate(type,year,id,2,date);
            
            return new ResponseEntity<String>(xml,new HttpHeaders(),HttpStatus.CREATED);
        }
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}/data.rdf", method = RequestMethod.GET, produces={"application/xml"})
        public ResponseEntity<String> exportDateToRDF(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd) throws JAXBException {
            LegislationService lds = new LegislationService();
            String rdfResult = lds.getRDFById(type,year,id);
            
            return new ResponseEntity<String>(rdfResult,new HttpHeaders(),HttpStatus.CREATED);
	
        }
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}/data.pdf", method = RequestMethod.GET, produces={"application/xml"})
        public ModelAndView exportDateToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd) {
                LegislationService lds = new LegislationService();
                String date = "";
                date += yyyy + "-" + mm + "-" + dd;
                LegalDocument legal = lds.getUpdatedById(type,year,id,2,date);
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/enacted/data.xml", method = RequestMethod.GET, produces={"application/xml"})
        public ResponseEntity<String> exportToXML(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws TransformerException{
            LegislationService lds = new LegislationService();
            String xml = lds.getXMLById(type,year,id,2);
            
            return new ResponseEntity<String>(xml,new HttpHeaders(),HttpStatus.CREATED);
        }
        
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/enacted/data.rdf", method = RequestMethod.GET,  produces={"application/xml"})
	public ResponseEntity<String> exportToRDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws JAXBException {
            LegislationService lds = new LegislationService();
            String rdfResult = lds.getRDFById(type,year,id);
            
            return new ResponseEntity<String>(rdfResult,new HttpHeaders(),HttpStatus.CREATED);
	
        }
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/enacted/data.pdf", method = RequestMethod.GET)
	public ModelAndView exportToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
                LegalDocument legal = lds.getById(type,year,id,2);
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.xml", method = RequestMethod.GET, produces={"application/xml"})
        public ResponseEntity<String> exportUpdatedToXML(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws TransformerException{
            LegislationService lds = new LegislationService();
            String xml = lds.getUpdatedXMLById(type,year,id,2);
            
            return new ResponseEntity<String>(xml,new HttpHeaders(),HttpStatus.CREATED);
        }
        
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.rdf", method = RequestMethod.GET,  produces={"application/xml"})
	public ResponseEntity<String> exportUpdatedToRDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) throws JAXBException {
            LegislationService lds = new LegislationService();
            String rdfResult = lds.getRDFById(type,year,id);
            
            return new ResponseEntity<String>(rdfResult,new HttpHeaders(),HttpStatus.CREATED);
	
        }
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.pdf", method = RequestMethod.GET)
	public ModelAndView exportUpdatedToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
                LegalDocument legal = lds.getUpdatedById(type,year,id,2,null);
		return new ModelAndView("pdfView", "legaldocument", legal);
	}
        
        @RequestMapping(value = "/legislation/search", method = RequestMethod.GET)
	public String search(@RequestParam Map<String,String> params, Model model) {
		if(params != null){
                    LegislationService lds = new LegislationService();
                    List<LegalDocument> LDs = lds.searchLegislation(params);
                    model.addAttribute("legalDocuments", LDs);
                }
		return "search";
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
	
}
