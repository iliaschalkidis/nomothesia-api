package com.di.nomothesia.controller;

import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.LegislationService;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 **/
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
            
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
                
                LegislationService lds = new LegislationService();
		List<String> tags = lds.getTags();
		List<LegalDocument> ldviewed = lds.MostViewed();
                List<LegalDocument> ldrecent = lds.MostRecent();
                model.addAttribute("serverTime", formattedDate);
		model.addAttribute("tags",tags);
                model.addAttribute("ldviewed",ldviewed);
                model.addAttribute("ldrecent",ldrecent);
                
		return "home";
                
	}
	
        @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
	public String aboutus(Locale locale, Model model) {	
		return "aboutus";
                
	}
        
        @RequestMapping(value = "/developer", method = RequestMethod.GET)
	public String developer(Locale locale, Model model) {	
		return "developer";
                
	}
}
