<%@page import="com.di.nomothesia.model.LegalDocument"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/resources/images/logo.png" >
        <title>NOMOΘΕΣΙΑ</title>

        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
        <link href='http://fonts.googleapis.com/css?family=Jura&subset=latin,greek' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Comfortaa&subset=latin,greek' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.css">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Load CSS -->
        <link href="${pageContext.servletContext.contextPath}/resources/css/navbar.css" rel="stylesheet"/>

        <!-- jQueryUI Calendar-->
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>  

        <style>
            #footer {
                position:absolute;
                width:100%;
                height:60px;   /* Height of the footer */
            }
        </style>    

    </head>
    <body>

        <!-- Navigation Bar -->
        <div id="custom-bootstrap-menu" class="navbar navbar-default " role="navigation">
            <div class="container-fluid">
                <div class="navbar-header"><a class="navbar-brand"  href="${pageContext.servletContext.contextPath}"><img style="height: 40px; margin-top: -10px;" src="${pageContext.servletContext.contextPath}/resources/images/logo.png"</img></a>
                    <a class="navbar-brand"  href="${pageContext.servletContext.contextPath}" style="font-family:'Jura'; font-size: 33px">ΝΟΜΟΘΕΣΙ@</a>
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-menubuilder">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>

                <div class="collapse navbar-collapse navbar-menubuilder">
                    <ul class="nav navbar-nav navbar-left">
                        <li>
                            <a href="${pageContext.servletContext.contextPath}" style="font-family: 'Comfortaa', cursive;">Αρχική</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/search" style="font-family: 'Comfortaa', cursive;" >Αναζήτηση</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/endpoint" style="font-family: 'Comfortaa', cursive;" >Endpoint</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/statistics" style="font-family: 'Comfortaa', cursive;" >Στατιστικά</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/aboutus" style="font-family: 'Comfortaa', cursive;" >Eμείς</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/developer" style="font-family: 'Comfortaa', cursive;" >Πληροφορίες</a>
                        </li>
                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en" style="font-family: 'Comfortaa', cursive;">EN</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Search Form -->
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="row" style="padding:10px;">
                        <div role="tabpanel">
                            <!-- Nav tabs -->
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#introduction" aria-controls="home" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">ΕΙΣΑΓΩΓΗ</a></li>
                                <li role="presentation"><a href="#legislation" aria-controls="profile" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">ΕΛΛΗΝΙΚΗ ΝΟΜΟΘΕΣΙΑ</a></li>
                                <li role="presentation"><a href="#uris" aria-controls="profile" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">ΕΞΥΠΝΑ URIS</a></li>
                                <li role="presentation"><a href="#restservices" aria-controls="profile" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">REST ΥΠΗΡΕΣΙΕΣ</a></li>
                            
                            </ul>

                            <!-- Tab panes -->
                            <div class="tab-content" style="text-align:justify;">
                                <div role="tabpanel" class="tab-pane fade in active" id="introduction">
                                    <br/>
                                    <p>Recently, there has been an increased interest in making government data open and easily accessible to the public. Technological advances in the area of semantic web have given rise to the development of the so-called Web of data, which has made an even stronger push to such efforts. The Web of data envisions an extension of the World Wide Web (WWW) that is composed of resources that correspond to real-world entities and are interconnected with links denoting their in-between relation. The concept of the Web of data has been recently incarnated into the research area of linked data that studies how one can make data expressed in the Resource Description Framework (RDF) available on the Web and interconnect it with other data with the aim of increasing its value for everybody. Therefore, semantic web technologies provide a perfect fit for making government data open.</p>
                                    <p>An important kind of government data is the data related to legislation. Legislation applies on every aspect of people's living and evolves continuously building a huge infrastructure of interlinked legal documents. Therefore, it is important for a government to offer services that make legislation easily accessible to the public and specialized professions aiming at informing themselves, defending their rights, citing, or using it as part of their job. Towards this direction, there are already many European Union (EU) countries that have computerized the legislative process by developing platforms for archiving legislation documents in information systems and offering online access to them.</p>      
                                    <p>Web standards like XML, XSLT, and RDF facilitate the separation between content, presentation, and metadata, thus contributing to a better exploitation of information present in these documents. A popular format and vocabulary that is used for the encoding of the structure and content of legal and paralegal documents is <a href="http://www.metalex.eu/">CEN MetaLex</a> (in the following just MetaLex), which has now been adopted and maintained by the European Committee for Standardization (CEN). MetaLex serves as an XML data exchange format, but recently an ontology counterpart of it has sprung up (<a href="http://justinian.leibnizcenter.org/MetaLex/metalexcen.owl">http://justinian.leibnizcenter.org/MetaLex/metalexcen.owl</a>) which is expressed in the Web Ontology Language (OWL) and used as a vocabulary for expressing metadata of legal documents.</p>
                                    <p>There are a number of endeavors that have employed MetaLex for organizing and offering national legislation to the public. For example, the MetaLex Document Server (<a href="http://doc.metalex.eu/">http://doc.metalex.eu/</a>) offers almost all the Dutch national regulations published by the official portal of the Dutch government. These regulations are offered both in MetaLex and as linked data in RDF. The United Kingdom also publishes legislation on its official portal (<a href="http://www.legislation.gov.uk/">http://www.legislation.gov.uk/)</a>. Legislation is expressed using MetaLex, while metadata information about them is offered in RDF using the MetaLex OWL ontology.</p>
                                    <p>In Greece, there is still very limited degree of computerization around the legislative process and even the discovery of legislation related to a specific topic can be a hard task. A recent law and program, called Diavgeia@ (<a href="https://diavgeia.gov.gr/">https://diavgeia.gov.gr/</a>), has tried to remedy this picture by obliging government institutions to upload their acts and decisions on the Web. Diavgeia@ portal offers basic search functionality using keywords on the content of legal documents and its metadata, as well as a service that provides developers the ability of searching this collection of documents based on some pre-selected metadata information.</p>
                                    <p>In this work, we are following the footsteps of other successful efforts in Europe and aim at modernizing the way the legislative work is offered to the public. In line with the goal of Diavgeia@, we envision a new state of affairs in which people have at their fingertips advanced search capabilities on the content of legislative work. We envision a paradigm of distribution of legislative work in a way that developers can consume, so that it can be also combined with other open data to increase its value in the interest of people. To the best of our knowledge, there is no other effort in Greece that takes this perspective on legislation and related decisions made by government institutions and administration alike.</p>
                                    <p>We view legislation as a collection of legal documents with a standard structure. Legal documents may be linked in terms of modifications by and citations to others, reflecting rich semantic information and interrelationships. A modern country needs intelligent services which not only present the textual contain of legal documents but are able to answer complex questions such as “Which legal documents a certain minister has signed during his service as Finance Minister?”, “Which legal documents have been modified and by whom?”, or “Retrieve the 10 most frequently modified legal documents between 2008-2013”.     To enable the formulation and answering of such complex questions, we have designed and developed a prototype web application, called Nomothesia, which offers Greek legislation in RDF as published in the government gazette. Greek legislation has been modeled in Nomothesia according to an OWL ontology that reuses the CEN MetaLex OWL ontology and extends it where needed to capture the peculiarities of Greek legislation. Nomothesia offers advanced presentational views and search functionality over the metadata and the textual content of Greek legislation. An important feature of Nomothesia is that it offers a SPARQL endpoint and a RESTful service that can be leveraged by developers for consuming its content programmatically and combine it with other open data. In this respect, Nomothesia opens a whole new market that can be developed based on semantic web technologies with direct societal benefits and great business opportunities.</p> 
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="legislation">
                                    <br/>
                                    <p>Greek legislation is published through different types of documents based on the government members, who curated it due to a specific legislative procedure. It has a standardized structure following the appropriate encoding, which may be reformed according to subsequent modifications. </p>  
                                    <h4>Structure of Greek Legislation </h4> 
                                    <p>There are five primary sources of Greek legislation we are considering in this work: constitution, presidential decrees, laws, acts of ministerial cabinet, and ministerial decisions. These sources of legislation are materialized in legal documents, which are encoded following specific standards.      Legislation is an event-driven process. Legal documents are published in the government gazette, while they may be modified by later legal documents in terms of content modifications, and finally come out of enforcement. In the course of this process, we need to capture the structural information of legislation and the evolution of its content through time, given by the legislative modifications applied on the primary legal document. </p> 
                                    <h4>Encoding of Greek Legislation </h4> 
                                    <p>Nowadays, the encoding of Greek legislation follows the rules set out in “Manual Directives for encoding of legislation” [5], which has been designed by the Central Committee of Encoding Standards and legislated in Law 2003/3133. The encoding of a legal document is organized in a tree hierarchy around the concept of fragments that are articles, paragraphs, cases, or passages. These fragments are described below.     Articles are the basic divisions in the text of legal document numbered using Arabic numerals (1, 2, 3, …) or, in the case of insertion of a new article in an existing legal document, by combining Arabic numeral with upper-case Greek letters (A, B, Γ, ...). An article may have a list of paragraphs that are numbered using Arabic numerals. If an article has a single paragraph, the numbering of that paragraph is omitted. Paragraphs may have a list of cases. Cases are numbered using lower-case Greek letters (α, β, γ, ...) and may have sub-cases which are numbered using double lower-case Greek letters (αα, ββ, ...). The verbal period between two dots is termed as passage. Passages are the elementary fragments of legal documents and are written contiguously, i.e., without any line breaks between them. Passages are the building blocks of cases and paragraphs. Last, legal documents may be subdivided according to their size at larger units, such as books, chapters, or sections, which are numbered using upper-case Greek letters. The larger units and articles may have title, which must be general and concise in order to bear their content, and is used in the systematic classification of the substance of legal documents     In addition to the aforementioned structural elements, legal documents are accompanied by metadata information. This includes the title of the legal document, which must be general enough but concise so as to reflect its content, the type (e.g., Law, Presidential Decree), the year of publication, and the number (i.e., the serial number counting from the begging of the year for each type). This last four pieces of metadata information serve also as a unique identifier of the legal document. Of equal importance are also the issue and the sheet number of the Government Gazette in which the legal document is published. </p>
                                    <h4>Citations</h4>  
                                    <p>When the reference to other legislation is necessary, this should be done uniformly throughout the text. Specifically, for purposes of accuracy and reading usability, and must bear the number of the legal document and the year of publication. At the first occurrence of the legal document, the issue and the number of the sheet of the Government Gazette must be stated in brackets. It should also be mentioned the fragment thereof, where such reference. </p>
                                    <h4> Legislative Modifications</h4>  
                                    <p>It is common international practice the amendment of a legal document by subsequent legal documents. Unfortunately, given the encoding of legal documents as described in Section 2.2, there is no standard methodology that is followed for the codification of this legislative concept. This makes the whole process of the amendment very challenging from our perspective.      By systematic observation, we reached to the conclusion that there are three main types of legislative modifications: 1) the substitution of a specific fragment by another introduced by a subsequent legal document, 2) the insertion of a new fragment and 3) the deletion of a specific fragment. All these kinds of modifications produce new versions of the original legal document. At any time point, the state of a legal document corresponds to the original document reformed by all subsequent modifications applied to it, until the specific time point.</p>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="uris">
                                    sdggggdgsddgsadsdsgdsgsdgdsgds
                                </div>
                                 <div role="tabpanel" class="tab-pane fade" id="restservices">
                                    sdggggdgsddgsadsdsgdsgsdgdsgds
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="footer" style="text-align: center; font-family:'Jura';" >
            <h5>Νομοθεσί@ &copy; 2014 - Τμήμα Πληροφορικής &amp; Τηλ/νωνιών ΕΚΠΑ - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.js"></script>

    </body>
</html>