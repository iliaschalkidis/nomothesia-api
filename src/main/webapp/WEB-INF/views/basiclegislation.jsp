<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
    <link href="${pageContext.servletContext.contextPath}/resources/css/bootstrap-social.css" rel="stylesheet"/>
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css" rel="stylesheet" type="text/css" />

    <!-- jQueryUI Calendar-->
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>  
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
 
    <script>
        $(function() {
            $('html, body').animate({scrollTop:$('#${id}').position().top}, 'slow');
            return false;
        });
    </script>
    
    <script>
        $(document).ready(function(){
	
            //Check to see if the window is top if not then display button
            $(window).scroll(function(){
                    if ($(this).scrollTop() > 100) {
                            $('.scrollToTop').fadeIn();
                    } else {
                            $('.scrollToTop').fadeOut();
                    }
            });

            //Click event to scroll to top
            $('.scrollToTop').click(function(){
                    $('html, body').animate({scrollTop : 0},800);
                    return false;
            });

        });
    </script>
    
    <script type="text/javascript" language="javascript" src="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.js"></script>

    
    <script> 
        function prepareList() {
            $('#messagescol').find('li:has(ul)')
            .click( function(event) {
                if (this == event.target) {
                    $(this).toggleClass('expanded');
                    $(this).children('ul').toggle('medium');
                }
                return false;
            })
            .addClass('collapsed')
            .children('ul').hide();
        };

        $(document).ready( function() {
            prepareList('&plusmn; ');
        });

//CollapsibleLists.applyTo(document.getElementById('messages'));
//$(function (){
//    $('#messagescol').find('li:has(ul)').click(function(event) {
//        event.stopPropagation();
//    $(event.target).children('ul').slideToggle();
//    });
//});
    </script>
    
</head>

<body <c:if test="${not empty legaldoc.getPlace()}">onload="initialize()"</c:if>>
    
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
                        <a href="${pageContext.servletContext.contextPath}/developer" style="font-family: 'Comfortaa', cursive;" >Πληροφορίες</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/aboutus" style="font-family: 'Comfortaa', cursive;" >Eμείς</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/legislation/statistics" style="font-family: 'Comfortaa', cursive;" >Στατιστικά</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/legislation/endpoint" style="font-family: 'Comfortaa', cursive;" >Endpoint</a>
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
                    <u style="color:  #1087dd">ΚΩΔΙΚΟΣ:</u> ${legaldoc.getDecisionType()}/${legaldoc.getYear()}/${legaldoc.getId()} <br/>
                    <u style="color:  #1087dd">ΗΜΕΡΟΜΗΝΙΑ:</u> ${legaldoc.getPublicationDate()} <br/>
                    <u style="color:  #1087dd">ΦΕΚ:</u> ${legaldoc.getFEK()}<br/>
                    <u style="color:  #1087dd">ΥΠΟΓΡΑΦΟΝΤΕΣ:</u><br/>
                    <c:forEach var="signer" items="${legaldoc.getSigners()}" varStatus="loop" begin="0" end="1">
                        ${signer.getFullName()}<br/>(${signer.getTitle()})<br/>     
                    </c:forEach>
                    <a data-toggle="collapse" href="#collapseExample" aria-expanded="false" aria-controls="collapseExample">[συνέχεια ...]</a>
                    <div class="collapse" id="collapseExample">
                    <c:forEach var="signer" items="${legaldoc.getSigners()}" varStatus="loop" begin="2">
                        ${signer.getFullName()}<br/>(${signer.getTitle()})<br/>     
                    </c:forEach>
                    </div><br/>
                    <c:if test="${not empty legaldoc.getTags()}">
                        <u style="color:  #1087dd">ΕΤΙΚΕΤΕΣ:</u> <br/>
                        <c:forEach items="${legaldoc.getTags()}" var="tag" varStatus="loop">
                            ${tag}<c:if test="${!loop.last}">,&nbsp;</c:if>
                        </c:forEach>
                    </c:if><br/>
                    <c:if test="${not empty legaldoc.getPlace()}">
                    <u style="color:  #1087dd">ΧΑΡΤΗΣ:</u><br/> <br/>
                    <div id="map" style="width:200; height:200px;"></div>
                    </c:if>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-default btn-lg" href="${requestScope['javax.servlet.forward.request_uri']}/enacted" style="width:100%">Αρxική Έκδοση</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-success btn-lg" href="${requestScope['javax.servlet.forward.request_uri']}/data.xml" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Εξαγωγή XML</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-danger btn-lg" href="${requestScope['javax.servlet.forward.request_uri']}/data.pdf" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Εξαγωγή PDF</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-primary btn-lg" href="${requestScope['javax.servlet.forward.request_uri']}/data.rdf" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Εξαγωγή RDF</a>
                </div>
            </div>
            <div class="col-md-9">
                <ul style="margin: 0; padding: 0; list-style-type: none; text-align: right;">
                    <li style="display: inline;"><a class="btn btn-social btn-xs btn-facebook"><i class="fa fa-facebook"></i>Share</a><!--<div class="fb-share-button" data-href="https://legislation.di.uoa.gr" data-layout="button_count"></div>--></li>
                    <li style="display: inline;"><a class="btn btn-social btn-xs btn-twitter"><i class="fa fa-twitter"></i>Tweet</a><!--<a class="twitter-share-button" href="https://twitter.com/share" data-related="twitterdev" data-count="horizontal">Tweet</a>--></li>
                </ul>
            <a href="#" class="scrollToTop"><img src="${pageContext.servletContext.contextPath}/resources/images/newup.png"/></a>
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
                    <div id="article-${article.getId()}">
                    <span style="text-align: center; font-size: 12px;"><h4>Άρθρο ${article.getId()}</h4></span>
                    <c:if test="${not empty article.getTitle()}">
                    <span style="text-align: center; font-size: 12px;"><h4>${article.getTitle()}</h4></span>
                    </c:if>
                    <br/>
                    <ol>
                    <c:forEach var="paragraph" items="${article.getParagraphs()}" varStatus="loop">
                        <li><div id="article-${article.getId()}-paragraph-${paragraph.getId()}" style="text-align: justify;">
                            <c:forEach var="passage" items="${paragraph.getPassages()}" varStatus="loop">
                                ${passage.getText()}
                            </c:forEach>
                            <ol style="list-style-type: lower-greek;">    
                            <c:forEach var="case1" items="${paragraph.getCaseList()}" varStatus="loop">
                                <li>
                                <c:forEach var="passage2" items="${case1.getPassages()}" varStatus="loop">
                                ${passage2.getText()}
                                </c:forEach>
                                <c:if test="${not empty case1.getCaseList()}">
                                    <ol style="list-style-type: lower-greek;">
                                <c:forEach var="case2" items="${case1.getCaseList()}" varStatus="loop">
                                    <c:forEach var="passage3" items="${case2.getPassages()}" varStatus="loop">
                                    <li>${passage3.getText()}</li>
                                    </c:forEach>
                                </c:forEach>
                                    </ol>
                                </c:if>
                                </li>
                            </c:forEach>
                            </ol>
                            <c:if test="${not empty paragraph.getTable()}">
                                <br/>${paragraph.getTable()}
                            </c:if>
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
                            </div></li><br/>
                    </c:forEach>
                    </ol>
                    </div>
                    <br/>
                </c:forEach>
                </div>
                  <div role="tabpanel" class="tab-pane" id="profile">
                      <table id="example" class="table table-striped table-bordered" style="text-align: left;" cellspacing="0" width="100%">
                        <thead>
                            <td>Έχοντας υπόψη:</td>
                        </thead>
                        <tbody>
                            <c:if test="${not empty legaldoc.getCitations()}">
                            <c:forEach var="citation" items="${legaldoc.getCitations()}" varStatus="loop">
                                <tr>
                                    <td>${citation.getDescription()}</td>
                                </tr>
                            </c:forEach>
                            </c:if>
                        </tbody>
                      </table>
                            
                  </div>
                <div role="tabpanel" class="tab-pane" id="messages">
                    <ul id="messagescol">
                    <c:forEach var="article" items="${legaldoc.getArticles()}" varStatus="loop">
                        <li><a href="${article.getURI()}" >Άρθρο ${article.getId()} <c:if test="${not empty article.getTitle()}"> «${article.getTitle()}»</c:if></a>
                    <ul>
                        <c:forEach var="paragraph" items="${article.getParagraphs()}" varStatus="loop">
                            <li><a href="${paragraph.getURI()}">Παράγραφος ${paragraph.getId()}</a></li>
                        </c:forEach>
                    </ul></li>
                    </c:forEach>
                    </ul>
                </div>
                <div role="tabpanel" class="tab-pane" id="settings">
                    <table id="example" class="table table-striped table-bordered" style="text-align: left;" cellspacing="0" width="100%">
                        <thead>
                            <td>Ημερομηνία</td>
                            <td>Τίτλος</td>
                            <td>ΦΕΚ</td>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${legaldoc.getPublicationDate()}</td>
                            <td>${legaldoc.getTitle()}</td>
                            <td>${legaldoc.getFEK()}</td>
                        </tr>
                    <c:if test="${not empty legalmods}">
                    <c:forEach var="version" items="${legalmods}" varStatus="loop">
                        <tr>
                            <td>${version.getPublicationDate()}</td>
                            <td>${version.getTitle()}</td>
                            <td>${version.getFEK()}</td>
                        </tr>
                    </c:forEach>
                    </c:if>
                        </tbody>
                    </table>
                    
                </div>
            </div>
        </div>
        </div>
        <div class="row"  style="height:400px;">&#160;&#160;</div>
        <div class="row" style="margin:10px; text-align: center; font-family:'Jura';">
            <h5>Νομοθεσί@ &copy; 2014 - Τμήμα Πληροφορικής &amp; Τηλ/νωνιών ΕΚΠΑ - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
        </div>
    </div>
<c:if test="${not empty legaldoc.getPlace()}">
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/geoxml3-kmz.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/ProjectedOverlay.js"></script>	

<script type="text/javascript">
    function initialize() {
        var myOptions = {
                zoom: 10,
                mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        // get KML filename
        var kml = '/resources/js/kml.xml';
        // create map
        var map = new google.maps.Map(document.getElementById("map"), myOptions);
        var myParser = new geoXML3.parser({map: map});
        myParser.parseKmlString("${legaldoc.getPlace()}");   
    }

</script>
</c:if>
</body>
</html>