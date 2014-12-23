/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.di.nomothesia.controller;

import com.di.nomothesia.model.LegalDocument;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Panagiotis
 */
public class XMLBuilder  {
        
    public String XMLbuilder (LegalDocument legald) throws TransformerConfigurationException, TransformerException {
        
        String xml = "";
        
        try {
 
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            //Root Element
            Element rootElement = doc.createElement("LegalDocument");
            rootElement.setAttribute("URI", legald.getURI());
            doc.appendChild(rootElement);

            //Metadata Branch
            Element Metadata = doc.createElement("Metadata");
            rootElement.appendChild(Metadata);
            
            Element title = doc.createElement("Title");
            title.setTextContent(legald.getTitle());
            Metadata.appendChild(title);
            
            Element year = doc.createElement("Year");
            year.setTextContent(legald.getYear());
            Metadata.appendChild(year);
            
            Element FEK = doc.createElement("FEK");
            FEK.setTextContent(legald.getFEK());
            Metadata.appendChild(FEK);
            
            Element type = doc.createElement("DecisionType");
            type.setTextContent(legald.getDecisionType());
            Metadata.appendChild(type);
            
            Element id = doc.createElement("Id");
            id.setTextContent(legald.getId());
            Metadata.appendChild(id);
            
            Element pdate = doc.createElement("PublicationDate");
            pdate.setTextContent(legald.getPublicationDate());
            Metadata.appendChild(pdate);
            
            //Signers Branch
            Element signers = doc.createElement("ListOfSigners");
            rootElement.appendChild(signers);
            
            for (int i = 0;i<legald.getSigners().size();i++) {
                
                //signers
                Element signer = doc.createElement("Signer");
                signers.appendChild(signer);
                
                //signers title
                Element signtitle = doc.createElement("Title");
                signtitle.setTextContent(legald.getSigners().get(i).getTitle());
                signer.appendChild(signtitle);
                
                //signers fullname
                Element signname = doc.createElement("FullName");
                signname.setTextContent(legald.getSigners().get(i).getFullName());
                signer.appendChild(signname);
                
            }
            
            //Citation Branch
            Element introduction = doc.createElement("Introduction");
            rootElement.appendChild(introduction);
            
            Element citation = doc.createElement("Citation");
            //NOT IMPLEMENTED YET
            //citation.setTextContent(legald.);
            introduction.appendChild(citation);
            
            //Body Branch
            //NOT IMPLEMENTED YET
            /*Element body = doc.createElement("Body");
            rootElement.appendChild(body);
            
            Element book = doc.createElement("Book");
            body.appendChild(book);
            
            Element section = doc.createElement("Section");
            book.appendChild(section);
            
            Element chapter = doc.createElement("Chapter");
            section.appendChild(chapter);
            
            Element part = doc.createElement("Part");
            chapter.appendChild(part);*/
            
            //Article Branch (inside body when body is completed)
            for (int i = 0;i<legald.getArticles().size();i++) {
                
                //article
                Element article = doc.createElement("Article");
                //article.setAttribute("number","" + legald.getArticles().get(i));
                article.setAttribute("URI", legald.getArticles().get(i).getURI());
                rootElement.appendChild(article);
                
                //NOT IMPLEMNTED YET
                //Element artTitle = doc.createElement("Title");
                //artTitle.setTextContent(legald.getArticles().get(i).getTitle());
                //article.appendChild(artTitle);
                
                //article id
                Element artId = doc.createElement("Id");
                artId.setTextContent("" + legald.getArticles().get(i).getId());
                article.appendChild(artId);
                
                for (int j = 0;j<legald.getArticles().get(i).getParagraphs().size();j++) {
                    
                    //paragraph
                    Element paragraph = doc.createElement("Paragraph");
                    paragraph.setAttribute("URI", legald.getArticles().get(i).getParagraphs().get(j).getURI());
                    //paragraph.setAttribute("number","" + legald.getArticles().get(i).getParagraphs().get(j));
                    
                    //paragraph id
                    Element parId = doc.createElement("Id");
                    parId.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getId());
                    paragraph.appendChild(artId);
                    
                    //NOT IMPLEMENTED YET
                    //Element parTable = doc.createElement("Table");
                    //parTable.setTextContent(legald.getArticles().get(i).getParagraphs().get(j).getTable());
                    //paragraph.appendChild(parTable);
                    
                    //paragraph modification
                    Element modification = doc.createElement("Modification");
                    //modification.setAttribute("URI", legald.getArticles().get(i).getParagraphs().get(j).getModification().getURI());
                    Element modType = doc.createElement("Type");
                    //modType.setTextContent(legald.getArticles().get(i).getParagraphs().get(j).getModification().getType());
                    modification.appendChild(modType);
                    paragraph.appendChild(modification);
                    
                    Element list = doc.createElement("List");
                    
                    for(int n = 0;n<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().size();n++) {
                        
                        //paragraph case list
                        paragraph.appendChild(list);
                        Element pcase = doc.createElement("Case");
                        pcase.setAttribute("URI", legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getURI());
                        list.appendChild(pcase);
                        
                        //case id
                        Element casId = doc.createElement("Id");
                        casId.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getId());
                        pcase.appendChild(casId);
                        
                        //case passages
                        for (int m = 0;m<legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getPassages().size();m++) {
                        
                            //case passages
                            Element cpassage = doc.createElement("Passage");
                            cpassage.setAttribute("URI", legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getPassages().get(m).getURI());

                            //case passage id
                            Element caseId = doc.createElement("Id");
                            caseId.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getPassages().get(m).getId());
                            cpassage.appendChild(caseId);

                            //case passage text
                            Element casText = doc.createElement("Text");
                            casText.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getCaseList().get(n).getPassages().get(m).getText());
                            cpassage.appendChild(casText);

                            pcase.appendChild(cpassage);
                    
                        }
                    
                    }
                    
                    for (int k = 0;k<legald.getArticles().get(i).getParagraphs().get(j).getPassages().size();k++) {
                        
                        //paragraph passages
                        Element passage = doc.createElement("Passage");
                        passage.setAttribute("URI", legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getURI());
                        
                        //passage id
                        Element pasId = doc.createElement("Id");
                        pasId.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getId());
                        passage.appendChild(pasId);
                        
                        //passage text
                        Element pasText = doc.createElement("Text");
                        pasText.setTextContent("" + legald.getArticles().get(i).getParagraphs().get(j).getPassages().get(k).getText());
                        passage.appendChild(pasText);
                        
                        paragraph.appendChild(passage);
                    
                    }
                    
                    article.appendChild(paragraph);
                
                }
            
            }
                 
            //Convert Document DOM type to String with greek characters support
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            Writer stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            transformer.transform(source, streamResult);       
            xml = stringWriter.toString();
            
            //DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
            //LSSerializer lsSerializer = domImplementation.createLSSerializer();
            //lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
            //xml = lsSerializer.writeToString(doc); 

      } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
      }
        return xml;
        
    }

}
