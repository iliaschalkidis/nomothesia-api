package com.di.nomothesia.controller;

import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.service.LegislationService;
import java.util.List;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LegislationController {
	
	private static final Logger logger = LoggerFactory.getLogger(LegislationController.class);
	
	@RequestMapping(value = "/legislation/{type}/{year}/{id}/enacted", method = RequestMethod.GET)
	public String presentOriginalLegalDocument(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                model.addAttribute("legaldoc", legaldoc);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("id","custom-bootstrap-menu");
		return "basiclegislation";
	}
        
        @RequestMapping(value = "en/legislation/{type}/{year}/{id}", method = RequestMethod.GET)
	public String presentUpdatedLegalDocumentEN(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("fragschanced", frags);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("id","custom-bootstrap-menu");
		return "basiclegislation_en";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}", method = RequestMethod.GET)
	public String presentUpdatedLegalDocument(@PathVariable String type, @PathVariable String year, @PathVariable String id, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("fragschanced", frags);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("id","custom-bootstrap-menu");
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{type1}/{id1}/{type2}/{id2}", method = RequestMethod.GET)
	public String presentLegalFragment(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String type1, @PathVariable String id1, @PathVariable String type2, @PathVariable String id2, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("fragschanced", frags);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("id", type1 + "-" + id1 + "-" +type2 + "-" + id2);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{type1}/{id1}", method = RequestMethod.GET)
	public String presentLegalFragmentless(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String type1, @PathVariable String id1, Model model) {
		LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("fragschanced", frags);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("id", type1 + "-" + id1);
		return "basiclegislation";
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}", method = RequestMethod.GET)
        public String presentModificationByDate(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd, Model model) {
                String date = "";
                date += yyyy + "-" + mm + "-" + dd;
                LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                if (legaldoc.getPublicationDate().compareTo(date) > 0){
                 legaldoc = null;
                }
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, date);
                List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
                model.addAttribute("legalmods", legalmods);
                model.addAttribute("fragschanced", frags);
                model.addAttribute("legaldoc", legaldoc);
                model.addAttribute("id","custom-bootstrap-menu");

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
        
        @RequestMapping(value="/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}/data.json", method = RequestMethod.GET)
	public @ResponseBody LegalDocument exportDateToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id,@PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd) {
                LegislationService lds = new LegislationService();
                String date = "";
                date += yyyy + "-" + mm + "-" + dd;
		LegalDocument legaldoc = lds.getById(type, year, id, 1);
                if (legaldoc.getPublicationDate().compareTo(date) > 0){
                 legaldoc = null;
                }
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, date);
                lds.getUpdatedById(legaldoc, legalmods);
                legaldoc.setPlace(null);
		return legaldoc;
 
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/{yyyy}-{mm}-{dd}/data.pdf", method = RequestMethod.GET, produces={"application/xml"})
        public ModelAndView exportDateToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd) {
                LegislationService lds = new LegislationService();
                String date = "";
                date += yyyy + "-" + mm + "-" + dd;
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                if (legaldoc.getPublicationDate().compareTo(date) > 0){
                 legaldoc = null;
                }
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, date);
                lds.getUpdatedById(legaldoc, legalmods);
		return new ModelAndView("pdfView", "legaldocument", legaldoc);
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
        
        @RequestMapping(value="/legislation/{type}/{year}/{id}/enacted/data.json", method = RequestMethod.GET)
	public @ResponseBody LegalDocument exportToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
		LegalDocument legal = lds.getById(type,year,id,2);
                legal.setPlace(null);
		return legal;
 
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
        
        @RequestMapping(value="/legislation/{type}/{year}/{id}/data.json", method = RequestMethod.GET)
	public @ResponseBody LegalDocument exportUpdatedToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, null);
                lds.getUpdatedById(legaldoc, legalmods);
                legaldoc.setPlace(null);
		return legaldoc;
 
	}
        
        @RequestMapping(value = "/legislation/{type}/{year}/{id}/data.pdf", method = RequestMethod.GET)
	public ModelAndView exportUpdatedToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id) {
                LegislationService lds = new LegislationService();
                LegalDocument legaldoc = lds.getById(type, year, id, 1);
                List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
                lds.getUpdatedById(legaldoc, legalmods);
		return new ModelAndView("pdfView", "legaldocument", legaldoc);
	}
        
        @RequestMapping(value = "/legislation/search", method = RequestMethod.GET)
	public String search(@RequestParam Map<String,String> params, Model model) {
		if(params != null){
                    LegislationService lds = new LegislationService();
                    List<LegalDocument> LDs = lds.searchLegislation(params);
                    List<String> tags = lds.getTags();
                    model.addAttribute("legalDocuments", LDs);
                    model.addAttribute("tags",tags);
                    
                    if((params.get("keywords")!=null) && !params.get("keywords").equals("")) {
                        model.addAttribute("keywords",params.get("keywords"));
                    }
                    
                    if((params.get("date")!=null) && !params.get("date").equals("")) {
                        model.addAttribute("date",params.get("date"));
                    }
                    
                    if((params.get("datefrom")!=null) && !params.get("datefrom").equals("")) {
                        model.addAttribute("datefrom",params.get("datefrom"));
                    }
                    
                    if((params.get("dateto")!=null) && !params.get("dateto").equals("")) {
                        model.addAttribute("dateto",params.get("dateto"));
                    }
                    
                    if((params.get("year")!=null) && !params.get("year").equals("")) {
                        model.addAttribute("year",params.get("year"));
                    }
                    
                    if((params.get("id")!=null) && !params.get("id").equals("")) {
                        model.addAttribute("id",params.get("id"));
                    }
                    
                    if((params.get("fek_year")!=null) && !params.get("fek_year").equals("")) {
                        model.addAttribute("fek_year",params.get("fek_year"));
                    }
                    
                    if((params.get("fek_id")!=null) && !params.get("fek_id").equals("")) {
                        model.addAttribute("fek_id",params.get("fek_id"));
                    }
                    
                    if((params.get("type")!=null) && !params.get("type").equals("")) {
                        model.addAttribute("type",params.get("type"));
                    }
                }
		return "search";
	}
        
        @RequestMapping(value = "/en/legislation/endpoint", method = RequestMethod.GET)
	public String endpointEN(@RequestParam Map<String,String> params, Model model) {
		if(params.get("query") != null){
                    LegislationService lds = new LegislationService();
                    EndpointResultSet eprs = lds.sparqlQuery(params.get("query"),params.get("format"));
                    model.addAttribute("endpointResults", eprs);
                    model.addAttribute("format", params.get("format"));
                }
		return "endpoint_en";
	}
        
        @RequestMapping(value = "/legislation/endpoint", method = RequestMethod.GET)
	public String endpoint(@RequestParam Map<String,String> params, Model model) {
		if(params.get("query") != null){
                    LegislationService lds = new LegislationService();
                    EndpointResultSet eprs = lds.sparqlQuery(params.get("query"),params.get("format"));
                    model.addAttribute("endpointResults", eprs);
                    model.addAttribute("format", params.get("format"));
                }
		return "endpoint";
	}
        
        
        @RequestMapping(value = "/en/legislation/endpoint/query/{id}", method = RequestMethod.GET)
	public String endpointEN( @PathVariable String id, Model model) {
		if(id != null){
                    LegislationService lds = new LegislationService();
                    EndpointResultSet eprs = lds.sparqlQuery(id,"HTML");
                    model.addAttribute("endpointResults", eprs);
                    //model.addAttribute("format", params.get("format"));
                }
		return "endpoint_en";
	}
        
        @RequestMapping(value = "/legislation/endpoint/query/{id}", method = RequestMethod.GET)
	public String endpoint( @PathVariable String id, Model model) {
		if(id != null){
                    LegislationService lds = new LegislationService();
                    EndpointResultSet eprs = lds.sparqlQuery(id,"HTML");
                    model.addAttribute("endpointResults", eprs);
                    //model.addAttribute("format", params.get("format"));
                }
		return "endpoint";
	}
	
       @ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
 
		//ModelAndView model = new ModelAndView("error/exception_error");
		return "error";
 
	}
        
}
