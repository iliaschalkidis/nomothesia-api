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
                <div class="navbar-header"><a class="navbar-brand"  href="${pageContext.servletContext.contextPath}/en"><img style="height: 40px; margin-top: -10px;" src="${pageContext.servletContext.contextPath}/resources/images/logo.png"</img></a>
                    <a class="navbar-brand"  href="${pageContext.servletContext.contextPath}/en" style="font-family:'Jura'; font-size: 33px">ΝΟΜΟΘΕΣΙ@</a>
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
                            <a href="${pageContext.servletContext.contextPath}/en" style="font-family: 'Comfortaa', cursive;">Home</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en/developer" style="font-family: 'Comfortaa', cursive;" >Information</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en/aboutus" style="font-family: 'Comfortaa', cursive;" >About Us</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en/legislation/search" style="font-family: 'Comfortaa', cursive;" >Search</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en/legislation/statistics" style="font-family: 'Comfortaa', cursive;" >Statistics</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/en/legislation/endpoint" style="font-family: 'Comfortaa', cursive;" >Endpoint</a>
                        </li>
                    </ul>
                    
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="${pageContext.servletContext.contextPath}" style="font-family: 'Comfortaa', cursive;">GR</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Search Form -->
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <div class="row" style="padding:10px;">
                        <div class="jumbotron" style="padding: 20px 20px 20px 20px;">
                            <form role="form" method="GET" action="${pageContext.servletContext.contextPath}/en/legislation/search">
                                <table width="100%">
                                    <tr>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">Keywords:</label>
                                                <input type="text" id="keywords" name="keywords" class="form-control" id="exampleInputEmail1" placeholder="Type keywords separated with (,)...">
                                            </div>
                                        </td>
                                        <td width="5%"></td>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputPassword1">Legislation Type:</label>
                                                <select class="form-control" name="type">
                                                    <option value="">-</option>
                                                    <option value="con">Constitution</option>
                                                    <option value="law">Law</option>
                                                    <option value="pd">PresidentiaL Decree (PD)</option>
                                                    <option value="amc">Act of Ministerial Cabinet (AMC)</option>
                                                    <option value="md">Ministerial Decision (MD)</option>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputPassword2">Publication ID:</label>
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <input class="form-control" value="" type="number" name="year" min="1976" max="2015" step="1">
                                                    </div>
                                                    <div class="col-md-1" style="font-size: 25px;">/</div>
                                                    <div class="col-md-5"> 
                                                        <input class="form-control" type="text" name="id" placeholder="N.">
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td width="5%"></td>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputPassword2">Publication Date:</label>
                                                <div class='input-group date' >
                                                    <input type='text'  name="date" id='datepicker' class="form-control" placeholder="Date"/>
                                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                </div> 
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td style="text-align: right;">
                                            <button type="submit" class="btn btn-primary btn-lg">Search</button>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td style="text-align: right; margin-top: 5px;">
                                            <a href="${pageContext.servletContext.contextPath}/en/legislation/search">Advanced Search</a> 
                                        </td>
                                    </tr>
                                </table>
                                        
                                <input type="hidden" name="datefrom" value="" class="form-control"/>
                                <input type="hidden" name="dateto" value="" class="form-control"/>
                                <input type="hidden" name="fek_isuue" value="" class="form-control"/>
                                <input type="hidden" name="fek_year" value="" class="form-control"/>
                                <input type="hidden" name="fek_id" value="" class="form-control"/>
                            </form>
                        </div>
                    </div>
                    
                    <div class="row" style="padding:10px;">
                        <div role="tabpanel">
                            <!-- Nav tabs -->
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">NEW LEGISLATION</a></li>
                                <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">MOST REQUESTED ACTS</a></li>
                            </ul>

                            <!-- Tab panes -->
                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane fade in active" id="home">
                                    <br/>
                                    <table id="example" class="table table-striped table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>ID</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ldrecent" items="${ldrecent}" varStatus="loop">
                                                <% LegalDocument ldr = (LegalDocument) pageContext.getAttribute("ldrecent");
                                                String[] URIsr = ldr.getURI().toString().split("uoa.gr/");
                                                pageContext.setAttribute("urir", URIsr[1]); %>
                                                <tr>
                                                    <td><a href="<c:url value="${pageContext.servletContext.contextPath}/en/legislation/${urir}"/>">${ldrecent.getTitle()}</a></td>
                                                    <td>${ldrecent.getDecisionType()} ${ldrecent.getYear()}/${ldrecent.getId()}</td>
                                                    <td>${ldrecent.getPublicationDate()}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>   
                                </div>

                                <div role="tabpanel" class="tab-pane fade" id="profile">
                                    <br/>
                                    <table id="example2" class="table table-striped table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>ID</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ldviewed" items="${ldviewed}" varStatus="loop">
                                                <% LegalDocument ldv = (LegalDocument) pageContext.getAttribute("ldviewed");
                                                String[] URIs = ldv.getURI().toString().split("uoa.gr/");
                                                pageContext.setAttribute("uri", URIs[1]); %>
                                                <tr>
                                                    <td><a href="<c:url value="${pageContext.servletContext.contextPath}/en/legislation/${uri}"/>">${ldviewed.getTitle()}</a></td>
                                                    <td>${ldviewed.getDecisionType()} ${ldviewed.getYear()}/${ldviewed.getId()}</td>
                                                    <td>${ldviewed.getPublicationDate()}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>   
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div style="text-align: justify;padding:10px;">
                        <p><img style="width:40%; margin-left: 10px; margin-bottom: 10px;" align="right" src="${pageContext.servletContext.contextPath}/resources/images/greekcloud.png"</img>
                        Στις μέρες μας, σε αντίθεση με πολλές χώρες της Ευρωπαϊκής Ένωσης, δεν υπάρχει κάποιο μηχανικά αναγνώσιμο πρότυπο για την ελληνική νομοθεσία και επομένως δεν μπορούν να προκύψουν προηγμένες διαδικτυακές νομικές υπηρεσίες. Στόχος μας είναι να συνεισφέρουμε στον τομέα της αναπαράστασης νομικής γνώσης και στην ενσωμάτωση αυτής στην περιοχή των ανοιχτών δεδομένων (open data) στην Ελλάδα, τόσο από τεχνολογική σκοπιά όσο και από άποψη διαφάνειας. Η συγκεκριμένη εργασία παρουσιάζει την Νομοθεσί@, μια πλατφόρμα, που σκοπό έχει να δώσει πρόσβαση στην ελληνική νομοθεσία, με την χρήση τεχνολογιών του <a href ="http://semanticweb.org/">Σημασιολογικού Ιστού (Semantic Web)</a> και την διασύνδεση τους με άλλα σύνολα δεδομένων (Καλλικράτης κ.α.) στα πλαίσια προώθησης της ηλεκτρονικής διακυβέρνησης. Η Νομοθεσί@ υιοθετεί την OWL οντολογία <a href="http://www.metalex.eu/">CEN Metalex</a>, ενώ παράλληλα επεξεργάζεται μια επέκταση της στα πλαίσια αναπαράστασης της ελληνική νομοθεσίας με τις όποιες ιδιορρυθμίες παρουσιάζει. Βασικός σκοπός είναι η αναπαράσταση των νομοθετικών σχέσεων και γεγονότων ως προς το περιεχόμενο (content) και τον χρόνο (temporal). Παράλληλα υψίστης σημασίας είναι η διασύνδεση των δεδομένων (linked data), η οποία αφορά τόσο στην διασύνδεση με άλλα δημόσια σύνολα δεδομένων <a href="http://www.linkedopendata.gr/">(Greek Linked Open Data)</a>, όσο και στην προώθηση της διασύνδεσης με το κοινοτικό δίκαιο στα πλαίσια της προώθηση του <a href="http://europa.eu/legislation_summaries/justice_freedom_security/judicial_cooperation_in_civil_matters/jl0068_en.htm">ELI (European Legislation Identifier)</a>. Πάνω σε αυτές τις αρχές, χτίζουμε μία διαδικτυακή διεπαφή προγραμματισμού εφαρμογών (αγγλ. API, από το Application Programming Interface) που στόχο έχει τόσο την δημοσίευση και παρουσίαση της ελληνικής νομοθεσίας στο εύρη κοινό μέσα από την συγκεκριμένη εύχρηστη μοντέρνα διαδικτυακή εφαρμογή, όσο και την δυνατότητα της χρήσης των ανοιχτών δεδομένων που προσφέρει το Νομοθεσί@ ως REST υπηρεσίες (εξαγωγή XML – RDF – PDF) με σκοπό την ανάπτυξη εφαρμογών ειδικού ενδιαφέροντος (νομικού, οικονομικού, δικαιωμάτων) από τρίτους.                        
                        </p>
                        <p>
                        Παρόμοιες πρωτοβουλίες στο εξωτερικό:
                            <ul>
                                <li><a href="http://doc.metalex.eu/" target="_blank">Metalex Document Server</a> (Netherlands)</li>
                                <li><a href="http://www.legislation.gov.uk/" target="_blank">Legislation.gov.uk</a> (United Kingdom)</li>
                            </ul>
                        </p>
                        <div align="center" style="padding:10px;">
                            <a class="btn btn-primary btn-lg" href="${pageContext.servletContext.contextPath}/en/legislation/legislation.owl" style="width:100%"><span class="glyphicon glyphicon-save" aria-hidden="true"></span> Download OWL Ontology</a>
                        </div>
                        <div align="center" style="padding:10px;">
                            <a class="btn btn-primary btn-lg" href="${pageContext.servletContext.contextPath}/en/legislation/legislation.n3" style="width:100%"><span class="glyphicon glyphicon-save" aria-hidden="true"></span> Download RDF Data Set</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="footer" style="text-align: center; font-family:'Jura';" >
            <h5>Νομοθεσί@ &copy; 2014 - Department of Informatics &amp; Telecommunications NKUA - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.js"></script>

        
      
        <script>
            $(document).ready(function() {

                $("#example").dataTable({

                    "autoWidth": false,    
                    "scrollY": "333px",
                    "scrollCollapse": true,
                    "paging": true,
                    "iDisplayLength": 4,
                    "aaSorting": [],
                    "bSortable": true,

                    "aoColumnDefs": [
                        { "aTargets": [ 0 ], "bSortable": true },
                        { "aTargets": [ 1 ], "bSortable": true },
                        { "aTargets": [ 2 ], "bSortable": true }],

                    "bLengthChange": false,

                    

                });

                $("#example2").dataTable({

                    "autoWidth": false,    
                    "scrollY": "333px",
                    "scrollCollapse": true,
                    "paging": true,
                    "iDisplayLength": 4,
                    "aaSorting": [],
                    "bSortable": true,

                    "aoColumnDefs": [
                        { "aTargets": [ 0 ], "bSortable": true },
                        { "aTargets": [ 1 ], "bSortable": true },
                        { "aTargets": [ 2 ], "bSortable": true }],

                    "bLengthChange": false,

                    
                });

            });
        </script>
   
        <script>
            $(function() {
                
                var availableTags = [
                <c:forEach var="tag" items="${tags}" >  
                    "${tag}", 
                </c:forEach>];
                    
                $( "#keywords" ).autocomplete({
                    source: availableTags
                });
            
            });
        </script>
    </body>
</html>