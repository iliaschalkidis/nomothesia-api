<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="en">
<head>
    
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/resources/images/logo.png" >
    <title><spring:message code="title"/></title>
    
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
    <!-- 
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>  
    -->
    <style>
        .tabs {
            min-height: 100%;
        }
        
        #footer {
            position:absolute;
            bottom:0;
            width:100%;
            height:60px;   /* Height of the footer */
            /*background:#6cf;*/
        }
    </style>

</head>

<body>
    
    <!-- Navigation Bar -->
    <div id="custom-bootstrap-menu" class="navbar navbar-default " role="navigation">
        <div class="container-fluid">
            <div class="navbar-header"><a class="navbar-brand"  href="${pageContext.servletContext.contextPath}"><img style="height: 40px; margin-top: -10px;" src="${pageContext.servletContext.contextPath}/resources/images/logo.png"</img></a>
            <a class="navbar-brand"  href="${pageContext.servletContext.contextPath}" style="font-family:'Jura'; font-size: 33px"><spring:message code="navbar.brand"/></a>
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
                            <a href="${pageContext.servletContext.contextPath}" style="font-family: 'Comfortaa', cursive;"><spring:message code="navbar.home"/></a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/search" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.search"/></a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/endpoint" style="font-family: 'Comfortaa', cursive;" >Endpoint</a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/legislation/statistics" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.statistics"/></a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/aboutus" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.aboutus"/></a>
                        </li>
                        <li>
                            <a href="${pageContext.servletContext.contextPath}/developer" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.info"/></a>
                        </li>
                    </ul>
                    
                    <ul class="nav navbar-nav navbar-right">
                        <c:set var="localeCode" value="${pageContext.response.locale}" />
                        <c:choose>
                            <c:when test="${localeCode == 'en' }"> 
                                <li>
                                    <a href="?language=el_GR" style="font-family: 'Comfortaa', cursive;">EL</a>
                                </li>
                            </c:when>
                            <c:when test="${localeCode == 'el_GR' }">
                                <li>
                                    <a href="?language=en" style="font-family: 'Comfortaa', cursive;">EN</a>
                                </li>
                            </c:when>
                        </c:choose>
                    </ul>
                </div>
        </div>
    </div>
      
    <!-- Search Form -->
    <div class="container">
        <div class="row" >
            <div class="col-md-2"></div>
            <div class="col-md-8">
            <div class="tabs" id="tabs"> 
              <ul style="margin: 0; padding: 0; list-style-type: none; text-align: center;"> 
                  <li style="display: inline; padding-right: 20px;"><a href="#tab1"><img src="${pageContext.servletContext.contextPath}/resources/images/ilias.jpg" width = "100" length="100" class="img-circle"></a></li>
                <li style="display: inline; padding-right: 20px;"><a href="#tab2"><img src="${pageContext.servletContext.contextPath}/resources/images/soursos.jpg" width = "100" length="100" class="img-circle"></a></li>
                <li style="display: inline; padding-right: 20px;"><a href="#tab3"><img src="${pageContext.servletContext.contextPath}/resources/images/charnik.jpg" width = "100" length="100" class="img-circle"></a></li>
                <li style="display: inline; padding-right: 20px;"><a href="#tab4"><img src="${pageContext.servletContext.contextPath}/resources/images/koubarak.jpg" width = "100" length="100" class="img-circle"></a></li>

              </ul>
              <div id="tab1">
                  <br/>  
                  <p>
                  <h4><spring:message code="halk.name"/></h4>
            <spring:message code="halk.status"/><br/>
            ihalk[at]di.uoa.gr <br/>&#160;<br/>
            <a href="thesis.pdf" target="_blank"><spring:message code="halk.pdf"/></a><br/>
            </p>
            <p style="text-align: justify"><spring:message code="halk.info"/><a href="http://madgik.di.uoa.gr/" target="_blank"> Management of Data and Information Knowledge Group (MaDgiK)</a></p>
            <p style="text-align: justify"><spring:message code="halk.info2"/></p>
            </div>
            <div id="tab2"><br/>
                    <p> <h4><spring:message code="sours.name"/></h4>
            <spring:message code="sours.status"/><br/>
            sdi08xxx[at]di.uoa.gr <br/>&#160;<br/>
            <a href="thesis.pdf" target="_blank"><spring:message code="sours.pdf"/></a><br/>
            </p>
            <p style="text-align: justify"><spring:message code="sours.info"/></p>
            <p style="text-align: justify"><spring:message code="sours.info2"/></p><br/>        
                </div>
                <div id="tab3">
                    <br/> 
            <p> <h4><spring:message code="char.name"/></h4>
            <spring:message code="char.status"/><br/>
            charnik[at]di.uoa.gr <br/>&#160;<br/>
            <spring:message code="char.pdf"/><br/>
            </p>
            <p style="text-align: justify"><spring:message code="char.info"/><a href="http://madgik.di.uoa.gr/" target="_blank"> Management of Data and Information Knowledge Group (MaDgiK)</a></p>
            <p style="text-align: justify"><spring:message code="char.info2"/></p><br/><br/>
                </div>
            <div id="tab4">
                <br/>
            <p> <h4><spring:message code="kouba.name"/></h4>
            <spring:message code="kouba.status"/><br/>
            koubarak[at]di.uoa.gr <br/>&#160;<br/>
            </p>
            <p style="text-align: justify"><spring:message code="kouba.info"/></p>
            <p><spring:message code="kouba.info2"/></p>
            <p style="text-align: justify"><spring:message code="kouba.info3"/></p><br/>
            </div>
            </div>    
                
            <!--    
            <p> <img src="${pageContext.servletContext.contextPath}/resources/images/ilias.jpg" style="padding:0 0 0px 0px;" align="right" width = "100" length="100" class="img-circle"><br/><br/>
            <h4>Ηλίας Χαλκίδης (Ilias Chalkidis)</h4>
            Mεταπτυχιακός Φοιτητής, Τμήμα Πληροφορικής & Τηλ/νωνιών, ΕΚΠΑ<br/>
            ihalk[at]di.uoa.gr <br/>&#160;<br/>
            <a href="thesis.pdf">Πτυχιακή Εργασία - "Νομοθεσί@: Πλατφόρμα για την Ελληνική νομοθεσία", Ηλίας Χαλκίδης (PDF)</a><br/>
            </p>
            <div id="accordion">
              <h3>Σύντομο Βιογραφικό</h3>
              <div>
            <p style="text-align: justify">Γεννήθηκε στην Αθήνα, Αττικής στις 08/02/1990. Είναι μεταπτυχιακός φοιτητής στο Τμήμα Πληροφορικής & Τηλεπικοινωνιών του Καποδιστριακού Πανεπιστημίου Αθηνών και παράλληλα εργάζεται ως ερευνητής στα πλαίσια της συγκεκριμένης εργασίας. Είναι μέλος του  <a href="http://madgik.di.uoa.gr/">Management of Data and Information Knowledge Group (MaDgiK)</a></p>
            <p style="text-align: justify">Επιμελήθηκε την δημιουργία της ηλεκτρονικής πλατφόρμας Νομοθεσί@ στα πλαίσια της πτυχιακής του εργασίας με τίτλο "Νομοθεσία: Πλατφόρμας για την Ελληνική νομοθεσία" με επιβλέποντες τον καθηγητή κ. <a href="http://cgi.di.uoa.gr/~koubarak/">Μανόλη Κουμπαράκη.</a> και τον διδακτορικό φοιτητή <a href="http://cgi.di.uoa.gr/~charnik/">Χαράλαμπο Νικολάου</a>. Μετά από σύσταση του καθηγητή κ.
            Μανόλη Κουμπαράκη, παρακολούθησε πέρσι το συνέδριο <a href="https://joinup.ec.europa.eu/community/semic/event/semic-2014-semantic-interoperability-conference"> SEMIC 2014 (Semantic Interoperability Community) </a>, που έλαβε χώρα στην Αθήνα, στον τομέα της Ηλεκτρονικής Διακυβέρνησης με χρήση με χρήση των τεχνολογιών του Σημασιολογικού Ιστού
(Semantic Web) και ευρήτερα των Διασυνδεδεμένων Δεδομένων (Linked Data). Πλέον συνεχίζει την περαιτέρω ανάπτυξη της πλατφόρμας και στο άμεσο μέλλον αποσκοπέι στην δημιουργία εργαλείου μετατροπής της ελληνικής νομοθεσίας σε RDF/OWL σύνολο δεδομένων (data set) με την υποστήριξη του Υπ. Διοικητικής Μεταρρύθμισης.</p><br/>
            </div>
            </div>
            <hr>
            <br/>
            <p> <img src="${pageContext.servletContext.contextPath}/resources/images/soursos.jpg" style="padding:0 0 0px 0px;" align="right" width = "100" length="100" class="img-circle"><br/><br/>
            <h4>Παναγιώτης Σούρσος (Panagiotis Soursos)</h4>
            Προπτυχιακός Φοιτητής, Τμήμα Πληροφορικής & Τηλ/νωνιών, ΕΚΠΑ<br/>
            sdi08xxx[at]di.uoa.gr <br/>&#160;<br/>
            <a href="thesis.pdf">Πτυχιακή Εργασία - "Νομοθεσί@ API: Αναδιοργάνωση της Ηλεκτρονικής Πλατφόρμας", Παναγιώτης Σούρσος (PDF)</a><br/>
            </p>
            <div id="accordion2">
              <h3>Σύντομο Βιογραφικό</h3>
              <div>
            <p style="text-align: justify">Γεννήθηκε στην Αθήνα, Αττικής στις 24/06/1990. Είναι προπτυχιακός φοιτητής στο Τμήμα Πληροφορικής & Τηλεπικοινωνιών του Καποδιστριακού Πανεπιστημίου Αθηνών.</p>
            <p style="text-align: justify">Επιμελήθηκε την αναδιοργάνωση της ηλεκτρονικής πλατφόρμας Νομοθεσί@ στα πλαίσια της πτυχιακής του εργασίας με τίτλο "Νομοθεσί@ API: Αναδιοργάνωση της Ηλεκτρονικής Πλατφόρμας" με επιβλέποντες τον καθηγητή κ. <a href="http://cgi.di.uoa.gr/~koubarak/">Μανόλη Κουμπαράκη</a> βάση της μετατροπής όλης της γνωστικής υποδομής σε RDF/OWL σύνολα δεδομένων, ενώ παράλληλα ανέπτυξε περαιτέρω τις λειτουργιές που παρέχει η ηλεκτρονική πλατφόρμα.</p><br/>
              </div>
            </div>
            <hr>
            <br/> 
            <p> <img src="${pageContext.servletContext.contextPath}/resources/images/charnik.jpg" style="padding:0 0 0px 0px;" align="right" width = "100" length="100" class="img-circle"><br/><br/>
            <h4>Χαράλαμπος Νικολάου (Charalampos Nikolaou)</h4>
            Υποψήφιος Διδάκτορας, Τμήμα Πληροφορικής & Τηλ/νωνιών, ΕΚΠΑ<br/>
            charnik[at]di.uoa.gr <br/>&#160;<br/>
            Διδακτορική Εργασία - "Χρονικής και Χωρική αναπαράστασης γνώσης σε RDF βάσεις δεδομένων", Χαράλαμπος Νικολάου<br/>
            </p>
            <div id="accordion3">
              <h3>Σύντομο Βιογραφικό</h3>
              <div>
            <p style="text-align: justify">Είναι υποψήφιος διδάκτορας στο Τμήμα Πληροφορικής & Τηλεπικοινωνιών (DIT) του Εθνικού και Καποδιστριακού Πανεπιστημίου Αθηνών (ΕΚΠΑ) με επιβλέπων τον καθηγητή Μανώλη Κουμπαράκη , ενώ μέλη της διδακτορικής επιτροπής του αποτελούν ο Βασίλης Χριστοφίδης και ο Philippe Rigaux. Το θέμα της διδακτορικής διατριβής του είναι "Χρονικής και Χωρική αναπαράστασης γνώσης σε RDF βάσεις δεδομένων". Είναι μέλος του  <a href="http://madgik.di.uoa.gr/">Management of Data and Information Knowledge Group (MaDgiK)</a></p>
            <p style="text-align: justify">Είχε τον ρόλο του επιβλέποντα στο πρώτο στάδιο δημιουργίας της ηλεκτρονικής πλατφόρμας Νομοθεσί@ στην πτυχιακή εργασία "Νομοθεσί@: Πλατφόρμα για την Ελληνική νομοθεσία".</p><br/><br/>
              </div>
            </div>
            <hr>
            <br/>
            <p> <img src="${pageContext.servletContext.contextPath}/resources/images/koubarak.jpg" style="padding:0 0 0px 0px;" align="right" width = "100" length="100" class="img-circle"><br/><br/>
            <h4>Μανόλης Κουμπαράκης (Manolis Koubarakis)</h4>
            Καθηγητής, Τμήμα Πληροφορικής & Τηλ/νωνιών, ΕΚΠΑ<br/>
            koubarak[at]di.uoa.gr <br/>&#160;<br/>
            </p>
            <div id="accordion4">
              <h3>Σύντομο Βιογραφικό</h3>
              <div>
            <p style="text-align: justify">Kατείχε στο παρελθόν θέσεις στο Τμήμα Ηλεκτρονικών Μηχανικών και Μηχανικών Υπολογιστών, Πολυτεχνείο Κρήτης (Επίκουρος και Αναπληρωτής Καθηγητής), στο Τμήμα Πληροφορικής του Πανεπιστημίου Αθηνών (Επισκέπτης Ερευνητής), στο Τμήμα Υπολογιστών του UMIST (νυν Πανεπιστήμιο Μάντσεστερ) (Λέκτορας) και στο Τμήμα Πληροφορικής του Imperial College του Λονδίνου (Επιστημονικός Συνεργάτης). Είναι κάτοχος Ph.D. στην Επιστήμη Υπολογιστών από το Εθνικό Μετσόβιο Πολυτεχνείο, με M.Sc. στην Επιστήμη Υπολογιστών από το Πανεπιστήμιο του Τορόντο.</p>
            <p> Έχει επιτελέσει μέλος της επιτροπής προγράμματος σε διάφορα διεθνή συνέδρια και ημερίδες και έχει οργανώσει διάφορες διεθνείς εκδηλώσεις.</a></p>
            <p style="text-align: justify">Έχει τον ρόλο του επιβλέποντα στην ανάπτυξη της ηλεκτρονικής πλατφόρμας Νομοθεσί@.</p><br/>
              </div>
            </div>
            <br/>-->
            </div>
            <div class="col-md-2"></div>
        </div>
        
    </div>
    
    <div id="footer" style="text-align: center; font-family:'Jura';" >
        <h5><spring:message code="footer"/> - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
    </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
    <script>
  $(function() {
    $( "#accordion" ).accordion({
         active: false,
         collapsible: true
    });
  });
  </script>
  <script>
  $(function() {
    $( "#accordion2" ).accordion({
         active: false,
         collapsible: true
    });
  });
  </script>
  <script>
  $(function() {
    $( "#accordion3" ).accordion({
         active: false,
         collapsible: true
    });
  });
  </script>
  <script>
  $(function() {
    $( "#accordion4" ).accordion({
         active: false,
         collapsible: true
    });
  });
  </script>
   <script>
  $(function() {
    $( "#tabs" ).tabs({collapsible: true, active: false });
  });
  </script>
</body>
</html>
