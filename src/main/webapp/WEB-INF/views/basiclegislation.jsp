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
                        <a href="${pageContext.servletContext.contextPath}/aboutus" style="font-family: 'Comfortaa', cursive;" >Πληροφορίες</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/contact" style="font-family: 'Comfortaa', cursive;" >Επικοινωνία</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/statistics" style="font-family: 'Comfortaa', cursive;" >Στατιστικά</a>
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
            <div class="col-md-3">  
                <div align="left" style="padding:10px;font-family: 'Comfortaa', cursive;">
                    <div align="center"><h4>ΓΕΝΙΚΑ ΣΤΟΙΧΕΙΑ</h4></div><br/>
                    ΚΩΔΙΚΟΣ: ${legaldoc.getDecisionType()}/${legaldoc.getYear()}/${legaldoc.getId()} <br/>
                    ΗΜΕΡΟΜΗΝΙΑ: ${legaldoc.getPublicationDate()} <br/>
                    ΦΕΚ: A'/${legaldoc.getYear()}/123 <br/>
                    ΥΠΟΓΡΑΦΟΝΤΕΣ:<br/> 
                    Αντώνης Σαμαράς <br/>
                    ΕΤΙΚΕΤΕΣ: <br/>
                    Περιβάλλον &amp; Φυσικοί Πόροι
                    
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-success btn-lg" href="downloadPDF" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Export to XML</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-danger btn-lg" href="${requestScope['javax.servlet.forward.request_uri']}/data.pdf" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Export to PDF</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-primary btn-lg" href="downloadPDF" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Export to RDF</a>
                </div>
            </div>
            <div class="col-md-9">
            <span style="text-align: center;"><h4>${legaldoc.getTitle()}</h4></span>
            <div role="tabpanel">

              <!-- Nav tabs -->
              <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">KEIMENO</a></li>
                <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">ΠΑΡΑΠΟΜΠΕΣ</a></li>
                <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">ΠΕΡΙΕΧΟΜΕΝΑ</a></li>
                <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">ΧΡΟΝΟΔΙΑΓΡΑΜΜΑ</a></li>
              </ul>

              <!-- Tab panes -->
              <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="home">
                <c:forEach var="article" items="${legaldoc.getArticles()}" varStatus="loop">
                    <span style="text-align: center; font-size: 12px;"><h4>Άρθρο ${article.getId()}</h4></span>
                    <br/>
                    <ol>
                    <c:forEach var="paragraph" items="${article.getParagraphs()}" varStatus="loop">
                        <li><div style="text-align: justify;">
                            <c:forEach var="passage" items="${paragraph.getPassages()}" varStatus="loop">
                                ${passage.getText()}
                            </c:forEach>
                            <ol style="list-style-type: lower-greek;">    
                            <c:forEach var="case1" items="${paragraph.getCaseList()}" varStatus="loop">
                                <c:forEach var="passage2" items="${case1.getPassages()}" varStatus="loop">
                                <li>${passage2.getText()}</li>
                                </c:forEach>
                            </c:forEach>
                            </ol>
                            <c:if test="${not empty paragraph.getModification()}">
                                <c:choose>
                                <c:when test="${paragraph.getModification().getType() == 'Case'}">
                                    "<c:forEach var="passage3" items="${paragraph.getModification().getFragment().getPassages()}" varStatus="loop">
                                    ${passage3.getText()}
                                    </c:forEach>"
                                </c:when>
                                <c:when test="${paragraph.getModification().getType() == 'Passage'}">
                                    "${paragraph.getModification().getFragment().getText()}"
                                </c:when>
                                <c:when test="${paragraph.getModification().getType() == 'Paragraph'}">
                                    "<c:forEach var="passage4" items="${paragraph.getModification().getFragment().getPassages()}" varStatus="loop">
                                    ${passage4.getText()}
                                    </c:forEach>
                                    <c:if test="${paragraph.getModification().getFragment().getCaseList().size() > 0}">
                                    <ol style="list-style-type: lower-greek;">    
                                    <c:forEach var="case2" items="${paragraph.getModification().getFragment().getCaseList()}" varStatus="loop">
                                        <c:forEach var="passage2" items="${case2.getPassages()}" varStatus="loop">
                                        <li>${passage2.getText()}</li>
                                        </c:forEach>
                                    </c:forEach>
                                    </ol>
                                    </c:if>"
                                    
                                </c:when>
                                </c:choose>
                            </c:if>
                            </div></li>
                    </c:forEach>
                    </ol>
                </c:forEach>
                </div>
                <div role="tabpanel" class="tab-pane" id="profile">...</div>
                <div role="tabpanel" class="tab-pane" id="messages">...</div>
                <div role="tabpanel" class="tab-pane" id="settings">...</div>
                </div>
            </div>
        </div>
        </div>
        <div class="row" style="margin:10px; text-align: center; font-family:'Jura';">
            <h5>Τμήμα Πληροφορικής &amp; Τηλ/νωνιών ΕΚΠΑ - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
        </div>
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
          $('#myTab a').click(function (e) {
              e.preventDefault()
              $(this).tab('show')
            })
            
            $('#myTab a[href="#profile"]').tab('show') // Select tab by name
            $('#myTab a:first').tab('show') // Select first tab
            $('#myTab a:last').tab('show') // Select last tab
            $('#myTab li:eq(2) a').tab('show') // Select third tab (0-indexed)
      </script>
</body>
</html>