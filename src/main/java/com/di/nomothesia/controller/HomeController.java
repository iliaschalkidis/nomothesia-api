package com.di.nomothesia.controller;

import com.di.nomothesia.model.LegalDocument;
import com.di.nomothesia.service.LegislationService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	public String home(Model model, Locale locale) {
                LegislationService lds = new LegislationService();
		List<String> tags = lds.getTags();
		List<LegalDocument> ldviewed = lds.MostViewed();
                List<LegalDocument> ldrecent = lds.MostRecent();
		model.addAttribute("tags",tags);
                model.addAttribute("ldviewed",ldviewed);
                model.addAttribute("ldrecent",ldrecent);
                model.addAttribute("locale",locale);
                
		return "home";
                
	}
	
        @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
	public String aboutus(Locale locale, Model model) {
            
            model.addAttribute("locale",locale);
            return "aboutus";
                
	}
        
        @RequestMapping(value = "/developer", method = RequestMethod.GET)
	public String developer(Locale locale, Model model) {	
	
            model.addAttribute("locale",locale);
            return "developer";
                
	}
        
        @RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String stats(Locale locale, Model model) {	
	
            model.addAttribute("locale",locale);
            return "stats";
                
	}
        
        @RequestMapping(value = "/legislation.owl", method = RequestMethod.GET)
	public void OwlDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
                
            int BUFFER_SIZE = 4096;
            String filePath = "/resources/datasets/legislation.owl";

            // get absolute path of the application
            //servletContext.contextPath
            ServletContext context = request.getSession().getServletContext();
            String appPath = context.getRealPath("");
            //System.out.println("appPath = " + appPath);

            // construct the complete absolute path of the file
            String fullPath = appPath + filePath;      
            File downloadFile = new File(fullPath);
            FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = context.getMimeType(fullPath);

            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            //System.out.println("MIME type: " + mimeType);

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();

        }
        
        @RequestMapping(value = "/legislation.n3", method = RequestMethod.GET)
	public void n3Download(HttpServletRequest request, HttpServletResponse response) throws IOException {
                
            int BUFFER_SIZE = 4096;
            String filePath = "/resources/datasets/legislation.n3";

            // get absolute path of the application
            //servletContext.contextPath
            ServletContext context = request.getSession().getServletContext();
            String appPath = context.getRealPath("");
            //System.out.println("appPath = " + appPath);

            // construct the complete absolute path of the file
            String fullPath = appPath + filePath;      
            File downloadFile = new File(fullPath);
            FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = context.getMimeType(fullPath);

            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            //System.out.println("MIME type: " + mimeType);

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();

        }
        
        @ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
 
            //ModelAndView model = new ModelAndView("error/exception_error");
            //model.addAttribute("locale",locale);
            return "error";
 
	}
            
}
