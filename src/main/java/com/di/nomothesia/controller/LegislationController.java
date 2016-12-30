package com.di.nomothesia.controller;

import com.di.nomothesia.model.EndpointResultSet;
import com.di.nomothesia.model.Fragment;
import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.model.Modification;
import com.di.nomothesia.service.LegislationServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
    
    //private static final Logger logger = LoggerFactory.getLogger(LegislationController.class);
    
    @RequestMapping (value = "/gazette/a/{year:\\d+}/{id:\\d+}", method = RequestMethod.GET)
    public void presentGovernmentGazettePDF(@PathVariable String year, @PathVariable String id, Model model,
                                            Locale locale, HttpServletResponse response) throws IOException {
        InputStream fis = null;
        fis = getClass().getResourceAsStream("file:/storage/nomothesia/resources/pdf/" + year + "/GG" + year + "_" + id + ".pdf");
        org.apache.commons.io.IOUtils.copy(fis, response.getOutputStream());
        response.setContentType("application/pdf");
        response.flushBuffer();
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/enacted", method = RequestMethod.GET)
    public String presentOriginalLegalDocument(@PathVariable String type, @PathVariable String year,
                                               @PathVariable String id, Model model, Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 1);
        model.addAttribute("legaldoc", legaldoc);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
        model.addAttribute("legalmods", legalmods);
        model.addAttribute("id", "custom-bootstrap-menu");
        model.addAttribute("locale", locale);
        
        if (legaldoc.getPublicationDate() == null) {
            return "error";
        }
        if (!legaldoc.getParts().isEmpty()) {
            return "basiclegislation3";
        } else if (legaldoc.getChapters().isEmpty()) {
            return "basiclegislation";
        } else {
            return "basiclegislation2";
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id}", method = RequestMethod.GET)
    public String presentUpdatedLegalDocument(@PathVariable String type, @PathVariable String year,
                                              @PathVariable String id, Model model, Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 1);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
        List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
        model.addAttribute("legalmods", legalmods);
        model.addAttribute("fragschanced", frags);
        model.addAttribute("legaldoc", legaldoc);
        model.addAttribute("id", "custom-bootstrap-menu");
        model.addAttribute("locale", locale);
        
        if (legaldoc.getPublicationDate() == null) {
            return "error";
        }
        if (!legaldoc.getParts().isEmpty()) {
            return "basiclegislation3";
        } else if (legaldoc.getChapters().isEmpty()) {
            return "basiclegislation";
        } else {
            return "basiclegislation2";
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{type1}/{id1}/{type2}/{id2}",
                     method = RequestMethod.GET)
    public String presentLegalFragment(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                                       @PathVariable String type1, @PathVariable String id1, @PathVariable String type2,
                                       @PathVariable String id2, Model model, Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 1);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
        List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
        model.addAttribute("legalmods", legalmods);
        model.addAttribute("fragschanced", frags);
        model.addAttribute("legaldoc", legaldoc);
        model.addAttribute("id", type1 + "-" + id1 + "-" + type2 + "-" + id2);
        model.addAttribute("locale", locale);
        
        if (legaldoc.getPublicationDate() == null) {
            return "error";
        }
        if (!legaldoc.getParts().isEmpty()) {
            return "basiclegislation3";
        } else if (legaldoc.getChapters().isEmpty()) {
            return "basiclegislation";
        } else {
            return "basiclegislation2";
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{type1}/{id1}", method = RequestMethod.GET)
    public String presentLegalFragmentless(@PathVariable String type, @PathVariable String year,
                                           @PathVariable String id, @PathVariable String type1,
                                           @PathVariable String id1, Model model, Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 1);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, null);
        List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
        model.addAttribute("legalmods", legalmods);
        model.addAttribute("fragschanced", frags);
        model.addAttribute("legaldoc", legaldoc);
        model.addAttribute("id", type1 + "-" + id1);
        model.addAttribute("locale", locale);
        
        if (legaldoc.getPublicationDate() == null) {
            return "error";
        }
        if (!legaldoc.getParts().isEmpty()) {
            return "basiclegislation3";
        } else if (legaldoc.getChapters().isEmpty()) {
            return "basiclegislation";
        } else {
            return "basiclegislation2";
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{yyyy:\\d+}-{mm:\\d+}-{dd:\\d+}",
                     method = RequestMethod.GET)
    public String presentModificationByDate(@PathVariable String type, @PathVariable String year,
                                            @PathVariable String id, @PathVariable String yyyy, @PathVariable String mm,
                                            @PathVariable String dd, Model model, Locale locale) {
        String date = "";
        date += yyyy + "-" + mm + "-" + dd;
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 1);
        if (legaldoc.getPublicationDate().compareTo(date) > 0) {
            legaldoc = null;
        }
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 1, date);
        List<Fragment> frags = lds.getUpdatedById(legaldoc, legalmods);
        model.addAttribute("legalmods", legalmods);
        model.addAttribute("fragschanced", frags);
        model.addAttribute("legaldoc", legaldoc);
        model.addAttribute("id", "custom-bootstrap-menu");
        model.addAttribute("locale", locale);
        
        if (legaldoc.getPublicationDate() == null) {
            return "error";
        }
        if (!legaldoc.getParts().isEmpty()) {
            return "basiclegislation3";
        } else if (legaldoc.getChapters().isEmpty()) {
            return "basiclegislation";
        } else {
            return "basiclegislation2";
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{yyyy:\\d+}-{mm:\\d+}-{dd:\\d+}/data.xml",
                     method = RequestMethod.GET, produces = {"application/xml"})
    public ResponseEntity<String> exportDateToXML(@PathVariable String type, @PathVariable String year,
                                                  @PathVariable String id, @PathVariable String yyyy,
                                                  @PathVariable String mm, @PathVariable String dd,
                                                  Locale locale) throws TransformerException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String date = "";
        date += yyyy + "-" + mm + "-" + dd;
        String xml = lds.getUpdatedXMLByIdDate(type, year, id, 2, date);
        
        return new ResponseEntity<>(xml, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{yyyy:\\d+}-{mm:\\d+}-{dd:\\d+}/data.rdf",
                     method = RequestMethod.GET, produces = {"application/xml"})
    public ResponseEntity<String> exportDateToRDF(@PathVariable String type, @PathVariable String year,
                                                  @PathVariable String id, @PathVariable String yyyy,
                                                  @PathVariable String mm, @PathVariable String dd,
                                                  Locale locale) throws JAXBException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String rdfResult = lds.getRDFById(type, year, id);
        
        return new ResponseEntity<>(rdfResult, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{yyyy:\\d+}-{mm:\\d+}-{dd:\\d+}/data.json",
                     method = RequestMethod.GET)
    public
    @ResponseBody
    LegalDocument exportDateToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                                   @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd,
                                   Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String date = "";
        date += yyyy + "-" + mm + "-" + dd;
        LegalDocument legaldoc = lds.getById(type, year, id, 2);
        if (legaldoc.getPublicationDate().compareTo(date) > 0) {
            legaldoc = null;
        }
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, date);
        lds.getUpdatedById(legaldoc, legalmods);
        legaldoc.setPlace(null);
        
        return legaldoc;
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/{yyyy:\\d+}-{mm:\\d+}-{dd:\\d+}/data.pdf",
                     method = RequestMethod.GET, produces = {"application/xml"})
    public ModelAndView exportDateToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                                        @PathVariable String yyyy, @PathVariable String mm, @PathVariable String dd,
                                        Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String date = "";
        date += yyyy + "-" + mm + "-" + dd;
        LegalDocument legaldoc = lds.getById(type, year, id, 2);
        if (legaldoc.getPublicationDate().compareTo(date) > 0) {
            legaldoc = null;
        }
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, date);
        lds.getUpdatedById(legaldoc, legalmods);
        
        if (!legaldoc.getParts().isEmpty()) {
            return new ModelAndView("pdfView3", "legaldocument", legaldoc);
        }
        if (!legaldoc.getChapters().isEmpty()) {
            return new ModelAndView("pdfView2", "legaldocument", legaldoc);
        } else {
            return new ModelAndView("pdfView", "legaldocument", legaldoc);
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/enacted/data.xml", method = RequestMethod.GET,
                     produces = {"application/xml"})
    public ResponseEntity<String> exportToXML(@PathVariable String type, @PathVariable String year,
                                              @PathVariable String id, Locale locale) throws TransformerException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String xml = lds.getXMLById(type, year, id, 2);
        
        return new ResponseEntity<>(xml, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/enacted/data.rdf", method = RequestMethod.GET,
                     produces = {"application/xml"})
    public ResponseEntity<String> exportToRDF(@PathVariable String type, @PathVariable String year,
                                              @PathVariable String id, Locale locale) throws JAXBException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String rdfResult = lds.getRDFById(type, year, id);
        
        return new ResponseEntity<>(rdfResult, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/enacted/data.json", method = RequestMethod.GET)
    public
    @ResponseBody
    LegalDocument exportToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                               Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legal = lds.getById(type, year, id, 2);
        legal.setPlace(null);
        
        return legal;
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/enacted/data.pdf", method = RequestMethod.GET)
    public ModelAndView exportToPDF(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                                    Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 2);
        
        if (!legaldoc.getParts().isEmpty()) {
            return new ModelAndView("pdfView3", "legaldocument", legaldoc);
        }
        if (!legaldoc.getChapters().isEmpty()) {
            return new ModelAndView("pdfView2", "legaldocument", legaldoc);
        } else {
            return new ModelAndView("pdfView", "legaldocument", legaldoc);
        }
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/data.xml", method = RequestMethod.GET,
                     produces = {"application/xml"})
    public ResponseEntity<String> exportUpdatedToXML(@PathVariable String type, @PathVariable String year,
                                                     @PathVariable String id,
                                                     Locale locale) throws TransformerException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String xml = lds.getUpdatedXMLById(type, year, id, 2);
        
        return new ResponseEntity<>(xml, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/data.rdf", method = RequestMethod.GET,
                     produces = {"application/xml"})
    public ResponseEntity<String> exportUpdatedToRDF(@PathVariable String type, @PathVariable String year,
                                                     @PathVariable String id, Locale locale) throws JAXBException {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        String rdfResult = lds.getRDFById(type, year, id);
        
        return new ResponseEntity<>(rdfResult, new HttpHeaders(), HttpStatus.CREATED);
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/data.json", method = RequestMethod.GET)
    public
    @ResponseBody
    LegalDocument exportUpdatedToJSON(@PathVariable String type, @PathVariable String year, @PathVariable String id,
                                      Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 2);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, null);
        lds.getUpdatedById(legaldoc, legalmods);
        legaldoc.setPlace(null);
        
        return legaldoc;
    }
    
    @RequestMapping (value = "/eli/{type}/{year:\\d+}/{id:\\d+}/data.pdf", method = RequestMethod.GET)
    public ModelAndView exportUpdatedToPDF(@PathVariable String type, @PathVariable String year,
                                           @PathVariable String id, Locale locale) {
        LegislationServiceImpl lds = new LegislationServiceImpl();
        LegalDocument legaldoc = lds.getById(type, year, id, 2);
        List<Modification> legalmods = lds.getAllModificationsById(type, year, id, 2, null);
        lds.getUpdatedById(legaldoc, legalmods);
        
        if (!legaldoc.getParts().isEmpty()) {
            return new ModelAndView("pdfView3", "legaldocument", legaldoc);
        }
        if (!legaldoc.getChapters().isEmpty()) {
            return new ModelAndView("pdfView2", "legaldocument", legaldoc);
        } else {
            return new ModelAndView("pdfView", "legaldocument", legaldoc);
        }
    }
    
    @RequestMapping (value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam Map<String, String> params, Model model, Locale locale) {
        if (params != null) {
            LegislationServiceImpl lds = new LegislationServiceImpl();
            List<LegalDocument> LDs = lds.searchLegislation(params);
            List<String> tags = lds.getTags();
            model.addAttribute("legalDocuments", LDs);
            model.addAttribute("tags", tags);
            model.addAttribute("locale", locale);
            
            if ((params.get("keywords") != null) && !params.get("keywords").equals("")) {
                model.addAttribute("keywords", params.get("keywords"));
            }
            
            if ((params.get("date") != null) && !params.get("date").equals("")) {
                model.addAttribute("date", params.get("date"));
            }
            
            if ((params.get("datefrom") != null) && !params.get("datefrom").equals("")) {
                model.addAttribute("datefrom", params.get("datefrom"));
            }
            
            if ((params.get("dateto") != null) && !params.get("dateto").equals("")) {
                model.addAttribute("dateto", params.get("dateto"));
            }
            
            if ((params.get("year") != null) && !params.get("year").equals("")) {
                model.addAttribute("year", params.get("year"));
            }
            
            if ((params.get("id") != null) && !params.get("id").equals("")) {
                model.addAttribute("id", params.get("id"));
            }
            
            if ((params.get("fek_year") != null) && !params.get("fek_year").equals("")) {
                model.addAttribute("fek_year", params.get("fek_year"));
            }
            
            if ((params.get("fek_id") != null) && !params.get("fek_id").equals("")) {
                model.addAttribute("fek_id", params.get("fek_id"));
            }
            
            if ((params.get("type") != null) && !params.get("type").equals("")) {
                model.addAttribute("type", params.get("type"));
            }
        }
        
        return "search";
    }
    
    @RequestMapping (value = "/endpoint", method = RequestMethod.GET)
    public String endpoint(@RequestParam Map<String, String> params, Model model, Locale locale) {
        if (params.get("query") != null) {
            LegislationServiceImpl lds = new LegislationServiceImpl();
            EndpointResultSet eprs = lds.sparqlQuery(params.get("query"), params.get("format"));
            model.addAttribute("endpointResults", eprs);
            model.addAttribute("format", params.get("format"));
        }
        model.addAttribute("locale", locale);
        
        return "endpoint";
    }
    
    @RequestMapping (value = "/endpoint/query/{id}", method = RequestMethod.GET)
    public String endpoint(@PathVariable String id, Model model, Locale locale) {
        if (id != null) {
            LegislationServiceImpl lds = new LegislationServiceImpl();
            EndpointResultSet eprs = lds.sparqlQuery(id, "HTML");
            model.addAttribute("endpointResults", eprs);
            model.addAttribute("locale", locale);
            //model.addAttribute("format", params.get("format"));
        }
        
        return "endpoint";
    }
    
    @ExceptionHandler (Exception.class)
    public String handleAllException(Exception ex) {
        //ModelAndView model = new ModelAndView("error/exception_error");
        //model.addAttribute("locale",locale);
        
        return "error";
    }
    
}
