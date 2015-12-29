<%@page import="com.di.nomothesia.model.Chapter"%>
<%@page import="com.di.nomothesia.model.Article"%>
<%@page import="com.di.nomothesia.model.Paragraph"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:set var="ionian_nums" value="${fn:split('α,β,γ,δ,ε,στ,ζ,η,θ,ι,ια,ιβ,ιγ,ιδ,ιε,ιστ,ιζ,ιη,ιθ,κ,κα,κβ,,κγ,κδ,κε,κστ,κζ,κη,κθ,λ,λα,λβ,,λγ,λδ,λε,λστ,λζ,λη,λθ', ',')}" scope="application" />
<c:set var="chap_nums" value="${fn:split('Α,Β,Γ,Δ,Ε,ΣΤ,Ζ,Η,Θ,Ι,ΙΑ,ΙΒ,ΙΓ,ΙΔ,ΙΕ,ΙΣΤ,ΙΖ,ΙΗ,ΙΘ', ',')}" scope="application" />
<!DOCTYPE html>
<html lang="en">
    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" href="/resources/images/logo.png" >
        <title><spring:message code="home.${legaldoc.getDecisionType()}"/> ${legaldoc.getYear()}/${legaldoc.getId()}</title>
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
        <link href="/resources/css/navbar.css" rel="stylesheet"/>
        <link href="/resources/css/bootstrap-social.css" rel="stylesheet"/>
        <link href="/resources/css/lightbox.css" rel="stylesheet">
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
            $(function () {
                $('html, body').animate({scrollTop: $('#${id}').position().top}, 'slow');
                return false;
            });
        </script>

        <script>
            $(document).ready(function () {

                //Check to see if the window is top if not then display button
                $(window).scroll(function () {
                    if ($(this).scrollTop() > 100) {
                        $('.scrollToTop').fadeIn();
                    } else {
                        $('.scrollToTop').fadeOut();
                    }
                });

                //Click event to scroll to top
                $('.scrollToTop').click(function () {
                    $('html, body').animate({scrollTop: 0}, 800);
                    return false;
                });

            });
        </script>

        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.js"></script>


        <script>
            function prepareList() {
                $('#messagescol').find('li:has(ul)')
                        .click(function (event) {
                            if (this == event.target) {
                                $(this).toggleClass('expanded');
                                $(this).children('ul').toggle('medium');
                            }
                            return false;
                        })
                        .addClass('collapsed')
                        .children('ul').hide();
            }
            ;

            $(document).ready(function () {
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

        <style>
            #footer {
                position:absolute;
                width:100%;
                height:60px;   /* Height of the footer */
                /*background:#6cf;*/
            }

            #share-buttons img {
                width: 35px;
                padding: 5px;
                border: 0;
                box-shadow: 0;
                display: inline;
            }

        </style>
    </style>

</head>

<body <c:if test="${not empty legaldoc.getPlace()}">onload="initialize()"</c:if>>

        <!-- Navigation Bar -->
        <div id="custom-bootstrap-menu" class="navbar navbar-default " role="navigation">
            <div class="container-fluid">
                <div class="navbar-header"><a class="navbar-brand"  href="/"><img style="height: 40px; margin-top: -10px;" src="/resources/images/logo.png"</img></a>
                <a class="navbar-brand"  href="/" style="font-family:'Jura'; font-size: 33px"><spring:message code="navbar.brand"/></a>
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
                        <a href="/" style="font-family: 'Comfortaa', cursive;"><spring:message code="navbar.home"/></a>
                    </li>
                    <li>
                        <a href="/search" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.search"/></a>
                    </li>
                    <li>
                        <a href="/endpoint" style="font-family: 'Comfortaa', cursive;" >Endpoint</a>
                    </li>
                    <li>
                        <a href="/statistics" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.statistics"/></a>
                    </li>
                    <li>
                        <a href="/aboutus" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.aboutus"/></a>
                    </li>
                    <li>
                        <a href="/developer" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.info"/></a>
                    </li>
                    <li>
                        <a href="/gazette" style="font-family: 'Comfortaa', cursive;" ><spring:message code="navbar.gazette"/></a>
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
        <div class="row">
            <div class="col-md-3">  
                <div align="left" style="padding:10px;font-family: 'Comfortaa', cursive;">
                    <div align="center"><h4><spring:message code="basic.data"/></h4></div><br/>
                    <u style="color:  #1087dd"><spring:message code="basic.code"/></u> <spring:message code="home.${legaldoc.getDecisionType()}"/>/${legaldoc.getYear()}/${legaldoc.getId()} <br/>
                    <u style="color:  #1087dd"><spring:message code="basic.date"/></u> ${legaldoc.getPublicationDate()} <br/>
                        <c:set var="repl" value='"' />
                        <c:choose>
                            <c:when test="${fn:contains(legaldoc.getFEK(),repl)}">
                                <c:set var="str" value="${fn:split(legaldoc.getFEK(),repl)}"/>
                                <c:set var="fek" value="${str[0]}"/>
                            </c:when>
                            <c:when test="${fn:contains(legaldoc.getFEK(),repl)==false}">
                                <c:set var="fek" value="${legaldoc.getFEK()}"/>
                            </c:when>
                        </c:choose>
                        <c:set var="fek2" value="${fn:split(fek, '/')}" />
                    <u style="color:  #1087dd"><spring:message code="basic.fek"/></u> <a href="/search?fek_issue=${fek2[0]}&fek_year=${fek2[1]}&fek_id=${fek2[2]}">${fek}</a> <a href="http://legislation.di.uoa.gr/gazette/a/${fek2[1]}/${fek2[2]}" target="_blank"><img height="15px" src="/resources/images/pdf-icon.jpg" alt="PDF" /></a><br/>
                    <u style="color:  #1087dd"><spring:message code="basic.signer"/></u><br/>
                        <c:forEach var="signer" items="${legaldoc.getSigners()}" varStatus="loop" begin="0" end="1">
                            ${signer.getFullName()}<br/> 
                    </c:forEach>
                    <a data-toggle="collapse" href="#collapseExample" aria-expanded="false" aria-controls="collapseExample">[συνέχεια ...]</a>
                    <div class="collapse" id="collapseExample">
                        <c:forEach var="signer" items="${legaldoc.getSigners()}" varStatus="loop" begin="2">
                            ${signer.getFullName()}<br/>
                        </c:forEach>
                    </div><br/>
                    <c:if test="${not empty legaldoc.getTags()}">
                        <u style="color:  #1087dd"><spring:message code="basic.labels"/></u> <br/>
                            <c:forEach items="${legaldoc.getTags()}" var="tag" varStatus="loop">
                                ${tag}<c:if test="${!loop.last}">,&nbsp;</c:if>
                        </c:forEach>
                    </c:if><br/>
                    <c:if test="${not empty legaldoc.getPlace()}">
                        <u style="color:  #1087dd"><spring:message code="basic.map"/></u><br/> <br/>
                        <div id="map" style="width:200; height:200px;"></div>
                    </c:if>
                </div>
                <div align="center" style="padding:10px;">
                    <c:choose>
                        <c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], '-')}">
                            <c:set var="base" value="${legaldoc.getURI()}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="base" value="${fn:replace(requestScope['javax.servlet.forward.request_uri'], '/enacted','')}" />
                        </c:otherwise>
                    </c:choose>                        
                    <a class="btn btn-default btn-lg" href="${base}/enacted" style="width:100%"><spring:message code="basic.enacted"/></a>
                </div>
                <div align="center" style="padding:10px;">
			<a class="btn btn-success btn-lg" target="_blank" href="${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}/data.xml" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> <spring:message code="basic.export"/> XML</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-danger btn-lg" target="_blank" href="${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}/data.pdf" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> <spring:message code="basic.export"/> PDF</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-primary btn-lg" target="_blank" href="${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}/data.rdf" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> <spring:message code="basic.export"/> RDF</a>
                </div>
                <div align="center" style="padding:10px;">
                    <a class="btn btn-warning btn-lg" target="_blank" href="${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}/data.json" style="width:100%"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> <spring:message code="basic.export"/> JSON</a>
                </div>
            </div>
            <div class="col-md-9">
                <!-- <ul style="margin: 0; padding: 0; list-style-type: none; text-align: right;">
                    <li style="display: inline;"><a class="btn btn-social btn-xs btn-facebook"><i class="fa fa-facebook"></i>Share</a><div class="fb-share-button" data-href="https://legislation.di.uoa.gr" data-layout="button_count"></div></li>
                    <li style="display: inline;"><a class="btn btn-social btn-xs btn-twitter"><i class="fa fa-twitter"></i>Tweet</a><a class="twitter-share-button" href="https://twitter.com/share" data-related="twitterdev" data-count="horizontal">Tweet</a></li>
                </ul>-->
                <a href="#" class="scrollToTop"><img src="/resources/images/newup.png"/></a>
                <span style="text-align: center;"><h4>${legaldoc.getTitle()}</h4></span>
                <div role="tabpanel">

                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#text" aria-controls="home" role="tab" data-toggle="tab"><spring:message code="basic.text"/></a></li>
                        <li role="presentation"><a href="#citations" aria-controls="profile" role="tab" data-toggle="tab"><spring:message code="basic.cit"/></a></li>
                        <li role="presentation"><a href="#list" aria-controls="messages" role="tab" data-toggle="tab"><spring:message code="basic.content"/></a></li>
                        <li role="presentation"><a href="#timeline" aria-controls="settings" role="tab" data-toggle="tab"><spring:message code="basic.timeline"/></a></li>
                        <li role="presentation"><a href="#images" aria-controls="images" role="tab" data-toggle="tab"><spring:message code="basic.images"/></a></li>
                        <li role="presentation"><a href="#problems" aria-controls="problems" role="tab" data-toggle="tab"><spring:message code="basic.problems"/></a></li>
                        <div id="share-buttons">
                            <li style="display: inline;">
                                <!-- Facebook -->
                                <a href="http://www.facebook.com/sharer.php?u=${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}" target="_blank"><img src="/resources/images/facebook.png" alt="Facebook" /></a>
                            </li>
                            <li style="display: inline;">
                                <!-- Twitter -->
                                <a href="http://twitter.com/share?url=${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}" target="_blank"><img src="/resources/images/twitter.png" alt="Twitter" /></a>
                            </li>
                        </div>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="text">
                            <c:if test="${empty legaldoc.getChapters()}">
                                <br/>
                                <div class="alert alert-warning" role="alert"><spring:message code="basic.notext"/></div>
                            </c:if>
                            <c:set var="chapcount" value="0"/>
                            <c:forEach var="chapter" items="${legaldoc.getChapters()}" varStatus="loop">
                                <c:set var="chapcount" value="${chapcount+1}"/>
                                <div id="chapter-${chapter.getId()}">
                                    <span style="text-align: center; font-size: 12px;"><h4><spring:message code="basic.chapter"/> ${chap_nums[chapter.getId()-1]}</h4></span>
                                    <c:if test="${not empty chapter.getTitle()}">
                                        <span style="text-align: center; font-size: 12px;"><h4>${chapter.getTitle()}</h4></span>
                                            </c:if>
                                    <br/>
                            <c:set var="artcount" value="0"/>
                            <c:forEach var="article" items="${chapter.getArticles()}" varStatus="loop">
                                <c:set var="artcount" value="${artcount+1}"/>
                                <div id="article-${article.getId()}">
                                    <span style="text-align: center; font-size: 12px;"><h4><spring:message code="basic.article"/> ${article.getId()}</h4></span>
                                    <c:if test="${not empty article.getTitle()}">
                                        <span style="text-align: center; font-size: 12px;"><h4>${article.getTitle()}</h4></span>
                                            </c:if>
                                    <br/>
                                    <ol>
                                        <c:set var="parcount" value="0"/>
                                        <c:forEach var="paragraph" items="${article.getParagraphs()}" varStatus="loop">
                                            <c:set var="parcount" value="${parcount+1}"/>
                                                <li${paragraph.getStatus() >= 1?' class="modification2"':"" }><div id="article-${article.getId()}-paragraph-${paragraph.getId()}" style="text-align: justify;">
                                                        <c:set var="pascount" value="0"/>
                                                        <c:forEach var="passage" items="${paragraph.getPassages()}" varStatus="loop">
                                                            <c:set var="pascount" value="${pascount+1}"/>
                                                            <c:if test="${passage.getStatus() >= 1}"><div class="modification"></c:if>${passage.getText()}
                                                           <c:if test="${not empty passage.getModification()}">
                                                                    <div class="mod">
                                                                    <c:choose>
                                                                        <c:when test="${passage.getModification().getType() == 'Case'}">
                                                                            <c:forEach var="passage3" items="${passage.getModification().getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage3.getText()}
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:when test="${passage.getModification().getType() == 'Passage'}">
                                                                            ${passage.getModification().getFragment().getText()}
                                                                        </c:when>
                                                                        <c:when test="${passage.getModification().getType() == 'Paragraph'}">
                                                                            ${passage.getModification().getFragment().getId()}. 
                                                                            <c:forEach var="passage4" items="${passage.getModification().getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage4.getText()}
                                                                            </c:forEach>
                                                                            <c:if test="${passage.getModification().getFragment().getCaseList().size() > 0}">
                                                                                <ol class="special-list" style="list-style-type: none;">   
                                                                                    <c:set var="casecount" value="0"/>
                                                                                    <c:forEach var="case2" items="${passage.getModification().getFragment().getCaseList()}" varStatus="loop">
                                                                                        <c:forEach var="passage2" items="${case2.getPassages()}" varStatus="loop">
                                                                                            <li data-number="${ionian_nums[casecount]}">${passage2.getText()}</li>
                                                                                        </c:forEach>
                                                                                        <c:set var="casecount" value="${casecount+1}"/>
                                                                                    </c:forEach>
                                                                                </ol>
                                                                            </c:if>
                                                                        </c:when>
                                                                        <c:when test="${passage.getModification().getType() == 'Article'}">
                                                                            <span style="text-align: center; font-size: 12px;"><h4><spring:message code="basic.article"/> ${passage.getModification().getFragment().getId()}</h4></span>
                                                                            <ol>
                                                                            <c:forEach var="paragraph5" items="${passage.getModification().getFragment().getParagraphs()}" varStatus="loop">
                                                                                <li>
                                                                                <c:forEach var="passage5" items="${paragraph5.getPassages()}" varStatus="loop">
                                                                                    ${passage5.getText()}
                                                                                </c:forEach>
                                                                                <c:if test="${paragraph5.getCaseList().size() > 0}">
                                                                                    <ol class="special-list" style="list-style-type: none;">   
                                                                                        <c:set var="casecount" value="0"/>
                                                                                        <c:forEach var="case5" items="${paragraph5.getCaseList()}" varStatus="loop">
                                                                                            <c:forEach var="passage6" items="${case5.getPassages()}" varStatus="loop">
                                                                                                <li data-number="${ionian_nums[casecount]}">${passage6.getText()}</li>
                                                                                            </c:forEach>
                                                                                            <c:set var="casecount" value="${casecount+1}"/>
                                                                                        </c:forEach>
                                                                                    </ol>
                                                                                </c:if>
                                                                                </li>
                                                                            </c:forEach>
                                                                            </ol>
                                                                        </c:when>
                                                                    </c:choose>
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${passage.getStatus() >= 1}">
                                                                    <c:if test="${passage.getStatus() ==2}">
                                                                        <span class="clickable" data-toggle="collapse" id="${artcount}${parcount}${pascount}" data-target=".${artcount}${parcount}${pascount}collapsed" style="text-align:right;"><span style="cursor: pointer;" class="glyphicon glyphicon-transfer" aria-hidden="true"></span></span>
                                                                            <c:set var="target_uri" value="chapter/${chapcount}article/${artcount}/paragraph/${parcount}/passage/${pascount}"/>
                                                                        <div class="collapse out budgets ${artcount}${parcount}${pascount}collapsed" style=" background-color: #FFCCCC; border: 6px solid; border-radius: 10px; border-color: #FFCCCC;">
                                                                            <c:forEach var="frag" items="${fragschanced}" varStatus="loop"> 
                                                                                <c:if test="${fn:endsWith(frag.getURI(),target_uri)}">
                                                                                    ${frag.getText()}
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </c:if>
                                                                </div>
                                                            </c:if>
                                                        </c:forEach>
                                                        <ol class="special-list" style="list-style-type: none;">
                                                            <c:set var="casecount" value="0"/>
                                                            <c:forEach var="case1" items="${paragraph.getCaseList()}" varStatus="loop">
                                                                    <li ${case1.getStatus() >= 1?' class="modification"':"" } data-number="${ionian_nums[casecount]}">
                                                                        <c:forEach var="passage2" items="${case1.getPassages()}" varStatus="loop">
                                                                            <c:if test="${passage2.getStatus() >= 1}">
                                                                                <div id="modification">
                                                                                </c:if>
                                                                                ${passage2.getText()}
                                                                                <c:if test="${passage2.getStatus() >= 1}">
                                                                                </div>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                        <c:if test="${not empty case1.getCaseList()}">
                                                                            <c:set var="casecount2" value="0"/>
                                                                            <ol style="list-style-type: none;">
                                                                                <c:forEach var="case2" items="${case1.getCaseList()}" varStatus="loop">
                                                                                    <c:if test="${case2.getStatus() >= 1}">
                                                                                        <div id="modification">
                                                                                        </c:if>
                                                                                        <c:forEach var="passage3" items="${case2.getPassages()}" varStatus="loop">
                                                                                            <c:if test="${passage3.getStatus() >= 1}">
                                                                                                <div id="modification">
                                                                                                </c:if>
                                                                                                <li data-number="${ionian_nums[casecount2]}">${passage3.getText()}</li>
                                                                                                    <c:if test="${passage3.getStatus() >= 1}">
                                                                                                </div>
                                                                                            </c:if>
                                                                                        </c:forEach>
                                                                                        <c:if test="${case2.getStatus() >= 1}">
                                                                                        </div>
                                                                                    </c:if>
                                                                                    <c:set var="casecount2" value="${casecount2+1}"/>
                                                                                </c:forEach>
                                                                            </ol>
                                                                        </c:if>
                                                                    <c:if test="${not empty case1.getModification()}">
                                                                    <div class="mod">
                                                                    <c:choose>
                                                                        <c:when test="${case1.getModification().getType() == 'Case'}">
                                                                            <c:forEach var="passage3" items="${case1.getModification().getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage3.getText()}
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:when test="${case1.getModification().getType() == 'Passage'}">
                                                                            ${case1.getModification().getFragment().getText()}
                                                                        </c:when>
                                                                        <c:when test="${case1.getModification().getType() == 'Paragraph'}">
                                                                            ${case1.getModification().getFragment().getId()}. 
                                                                            <c:forEach var="passage4" items="${case1.getModification().getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage4.getText()}
                                                                            </c:forEach>
                                                                            <c:if test="${case1.getModification().getFragment().getCaseList().size() > 0}">
                                                                                <ol class="special-list" style="list-style-type: none;">   
                                                                                    <c:set var="casecount" value="0"/>
                                                                                    <c:forEach var="case2" items="${case1.getModification().getFragment().getCaseList()}" varStatus="loop">
                                                                                        <c:forEach var="passage2" items="${case2.getPassages()}" varStatus="loop">
                                                                                            <li data-number="${ionian_nums[casecount]}">${passage2.getText()}</li>
                                                                                        </c:forEach>
                                                                                        <c:set var="casecount" value="${casecount+1}"/>
                                                                                    </c:forEach>
                                                                                </ol>
                                                                            </c:if>
                                                                        </c:when>
                                                                        <c:when test="${case1.getModification().getType() == 'Article'}">
                                                                            <span style="text-align: center; font-size: 12px;"><h4><spring:message code="basic.article"/> ${case1.getModification().getFragment().getId()}</h4></span>
                                                                            <ol>
                                                                            <c:forEach var="paragraph5" items="${case1.getModification().getFragment().getParagraphs()}" varStatus="loop">
                                                                                <li>
                                                                                <c:forEach var="passage5" items="${paragraph5.getPassages()}" varStatus="loop">
                                                                                    ${passage5.getText()}
                                                                                </c:forEach>
                                                                                <c:if test="${paragraph5.getCaseList().size() > 0}">
                                                                                    <ol class="special-list" style="list-style-type: none;">   
                                                                                        <c:set var="casecount" value="0"/>
                                                                                        <c:forEach var="case5" items="${paragraph5.getCaseList()}" varStatus="loop">
                                                                                            <c:forEach var="passage6" items="${case5.getPassages()}" varStatus="loop">
                                                                                                <li data-number="${ionian_nums[casecount]}">${passage6.getText()}</li>
                                                                                            </c:forEach>
                                                                                            <c:set var="casecount" value="${casecount+1}"/>
                                                                                        </c:forEach>
                                                                                    </ol>
                                                                                </c:if>
                                                                                </li>
                                                                            </c:forEach>
                                                                            </ol>
                                                                        </c:when>
                                                                    </c:choose>
                                                                    </div>
                                                                </c:if>
                                                                    <c:if test="${case1.getStatus() >= 1}">
                                                                        <c:if test="${case1.getStatus() ==2}">
                                                                            <span class="clickable" data-toggle="collapse" id="${artcount}${parcount}0${casecount+1}" data-target=".${artcount}${parcount}0${casecount+1}collapsed" style="text-align:right;"><span style="cursor: pointer;" class="glyphicon glyphicon-transfer" aria-hidden="true"></span></span>
                                                                                <c:set var="target_uri" value="chapter/${chapcount}article/${artcount}/paragraph/${parcount}/case/${casecount+1}"/>
                                                                            <div class="collapse out budgets ${artcount}${parcount}0${casecount+1}collapsed" style=" background-color: #FFCCCC; border: 6px solid; border-radius: 10px; border-color: #FFCCCC;">
                                                                                <c:forEach var="frag" items="${fragschanced}" varStatus="loop"> 
                                                                                    <c:if test="${fn:endsWith(frag.getURI(),target_uri)}">
                                                                                        <c:forEach var="passage23" items="${frag.getPassages()}" varStatus="loop">
                                                                                            ${passage23.getText()}
                                                                                        </c:forEach>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </div>
                                                                        </c:if>
                                                                   
                                                                </c:if>
                                                            </li>
                                                                <c:set var="casecount" value="${casecount+1}"/>        
                                                            </c:forEach>
                                                        </ol>
                                                        <c:if test="${not empty paragraph.getTable()}">
                                                            <br/>${paragraph.getTable()}
                                                        </c:if>
                                                        
                                                    </div>
                                                    <c:if test="${paragraph.getStatus() >= 1}">
                                                        <c:if test="${paragraph.getStatus() ==2}">
                                                        <span class="clickable" data-toggle="collapse" id="${parcount}" data-target=".${parcount}collapsed" style="text-align:right;"><span style="cursor: pointer;" class="glyphicon glyphicon-transfer" aria-hidden="true"></span></span>
                                                            <c:set var="target_uri" value="chapter/${chapcount}article/${artcount}/paragraph/${parcount}"/>
                                                        <div class="collapse out budgets ${parcount}collapsed" style=" background-color: #FFCCCC; padding: 6px; border-radius: 10px;">
                                                            <c:forEach var="frag" items="${fragschanced}" varStatus="loop"> 
                                                                <c:if test="${fn:endsWith(frag.getURI(),target_uri)}">
                                                                    <c:forEach var="passage24" items="${frag.getPassages()}" varStatus="loop">
                                                                        ${passage24.getText()}
                                                                    </c:forEach>
                                                                    <c:if test="${frag.getCaseList().size() > 0}">
                                                                        <ol class="special-list" style="list-style-type: none;">
                                                                            <c:set var="casecount" value="0"/>
                                                                            <c:forEach var="case22" items="${frag.getCaseList()}" varStatus="loop">
                                                                                <c:forEach var="passage22" items="${case22.getPassages()}" varStatus="loop">
                                                                                    <li data-number="${ionian_nums[casecount]}">${passage22.getText()}</li>
                                                                                    </c:forEach>
                                                                                    <c:set var="casecount2" value="${casecount2+1}"/>
                                                                                </c:forEach>
                                                                        </ol>
                                                                    </c:if>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </c:if>
                                            </c:if></li>
                                            <br/>
                                        </c:forEach>
                                    </ol>
                                </div>
                                <br/>
                            </c:forEach>
                            </div>
                        </c:forEach>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="citations">
                        <c:if test="${not empty legaldoc.getCitations()}">
                        <div class="table-responsive">
                            <table id="example" class="table table-striped table-bordered" style="text-align: left;" cellspacing="0" width="100%">
                                <thead>
                                <td><spring:message code="basic.mind"/></td>
                                </thead>
                                <tbody>
                                        <c:forEach var="citation" items="${legaldoc.getCitations()}" varStatus="loop">
                                            <tr>
                                                <td>${citation.getDescription()}</td>
                                            </tr>
                                        </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        </c:if>
                        <c:if test="${empty legaldoc.getCitations()}">
                             <br/>
                            <div class="alert alert-warning" role="alert"><spring:message code="basic.nocitations"/></div>
                        </c:if>       
                        </div>
                        <div role="tabpanel" class="tab-pane" id="list">
                            <ul id="messagescol">
                                <c:forEach var="chapter" items="${legaldoc.getChapters()}" varStatus="loop">
                                    <% Chapter c = (Chapter) pageContext.getAttribute("chapter");

                                            //String[] URIsr = a.getURI().toString().split("uoa.gr/");
                                            //pageContext.setAttribute("urir", URIsr[1]);

                                        %>
                                        <li ><a href="<c:url value="${legaldoc.getId()}/chapter/${chapter.getId()}"/>" target="_blank"> <spring:message code="basic.chapter"/> ${chapter.getId()} <c:if test="${not empty chapter.getTitle()}"> «${chapter.getTitle()}»</c:if></a>
                                                <ul>
                                    <c:forEach var="article" items="${chapter.getArticles()}" varStatus="loop">
                                        <% Article a = (Article) pageContext.getAttribute("article");

                                            //String[] URIsr = a.getURI().toString().split("uoa.gr/");
                                            //pageContext.setAttribute("urir", URIsr[1]);

                                        %>
                                        <li ><a href="<c:url value="${legaldoc.getId()}/chapter/${chapter.getId()}/article/${article.getId()}"/>" target="_blank"> <spring:message code="basic.article"/> ${article.getId()} <c:if test="${not empty article.getTitle()}"> «${article.getTitle()}»</c:if></a>
                                                <ul>
                                                <c:forEach var="paragraph" items="${article.getParagraphs()}" varStatus="loop">
                                                    <% Paragraph p = (Paragraph) pageContext.getAttribute("paragraph");

                                                        String[] URIs1 = p.getURI().toString().split("/article/");
                                                        pageContext.setAttribute("uri1", URIs1[1]);

                                                    %>
                                                    <li><a href="<c:url value="${legaldoc.getId()}/chapter/${chapter.getId()}/article/${uri1}"/>" target="_blank"><spring:message code="basic.par"/> ${paragraph.getId()}</a></li>
                                                    </c:forEach>
                                                </ul>
                                        </li>
                                    </c:forEach>
                                </ul>
                                        </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="timeline">
                            <div class="table-responsive">
                            <table id="example" class="table table-striped table-bordered" style="text-align: left;" cellspacing="0" width="100%">
                                <thead>
                                <td><spring:message code="home.datesimple"/></td>
                                <td><spring:message code="home.title"/></td>
                                <td><spring:message code="basic.fek2"/></td>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>${legaldoc.getPublicationDate()}</td>
                                        <td>${legaldoc.getTitle()}</td>
                                        <td>${legaldoc.getFEK()}</td>
                                    </tr>
                                    <c:if test="${not empty legalmods}">
                                        <c:set var="currentTitle" value=""/>
                                        <c:forEach var="legalmod" items="${legalmods}" varStatus="loop">
                                            <c:if test="${legalmod.getCompetenceGround().getTitle()!=currentTitle}"> 

                                                <tr class="clickable" data-toggle="collapse" id="mod" data-target=".modcollapsed">
                                                    <td>${legalmod.getCompetenceGround().getPublicationDate()}</td>
                                                    <td><a href="${legalmod.getCompetenceGround().getURI()} ">${legalmod.getCompetenceGround().getTitle()}</a> <span style="cursor: pointer;" class="glyphicon glyphicon-download" aria-hidden="true"></span></td>
                                                    <td>${legalmod.getCompetenceGround().getFEK()}</td>
                                                </tr>
                                            </c:if>
                                            <tr class="collapse out budgets modcollapsed">
                                                <td colspan="3">
                                                    <div class="table-responsive">
                                                    <table id="example2" class="table table-striped table-bordered" style="text-align: left;" cellspacing="0" width="100%">
                                                        <thead>
                                                            <spring:message var="par" code='basic.par'/>
                                                            <spring:message var="pas" code='basic.pas'/>
                                                            <spring:message var="case2" code='basic.case'/>
                                                            <spring:message var="art" code='basic.article'/>
                                                            <spring:message var="chap" code='basic.chapter'/>
                                                            <c:set var="modname" value="${fn:replace(legalmod.getTarget(),'paragraph',par)}"/>
                                                            <c:set var="modname" value="${fn:replace(modname,'passage',pas)}"/>
                                                            <c:set var="modname" value="${fn:replace(modname,'case',case2)}"/>
                                                            <c:set var="modname" value="${fn:replace(modname,'article',art)}"/>
                                                            <c:set var="modname" value="${fn:replace(modname,'chapter',chap)}"/>
                                                        <td><spring:message code="basic.mod"/> ${modname}</td>
                                                        <td><spring:message code="basic.type"/></td>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${legalmod.getFragment().getType() == 'Case'}">
                                                                            <c:forEach var="passage13" items="${legalmod.getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage13.getText()}
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:when test="${legalmod.getFragment().getType() == 'Passage'}">
                                                                            ${legalmod.getFragment().getText()}
                                                                        </c:when>
                                                                        <c:when test="${legalmod.getFragment().getType() == 'Paragraph'}">
                                                                            <c:forEach var="passage14" items="${legalmod.getFragment().getPassages()}" varStatus="loop">
                                                                                ${passage14.getText()}
                                                                            </c:forEach>
                                                                            <c:if test="${legalmod.getFragment().getCaseList().size() > 0}">
                                                                                <ol style="list-style-type: lower-greek;">    
                                                                                    <c:forEach var="case12" items="${legalmod.getFragment().getCaseList()}" varStatus="loop">
                                                                                        <c:forEach var="passage12" items="${case12.getPassages()}" varStatus="loop">
                                                                                            <li>${passage12.getText()}</li>
                                                                                            </c:forEach>
                                                                                        </c:forEach>
                                                                                </ol>
                                                                            </c:if>
                                                                        </c:when>
                                                                    </c:choose>
                                                                </td>
                                                                <td width="20%">
                                                                    <c:choose>
                                                                        <c:when test="${fn:endsWith(legalmod.getType(),'Edit')}">
                                                                            <spring:message code="basic.rep"/>
                                                                        </c:when>
                                                                        <c:when test="${fn:endsWith(legalmod.getType(),'Addition')}">
                                                                            <spring:message code="basic.ins"/>
                                                                        </c:when>
                                                                        <c:when test="${fn:endsWith(legalmod.getType(),'Delete')}">
                                                                            <spring:message code="basic.del"/>
                                                                        </c:when>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                    </table>
                                                    </div>
                                                </td>
                                            </tr>
                                            <c:set var="currentTitle" value="${legalmod.getCompetenceGround().getTitle()}"/>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                            </div>        
                        </div>
                        <div role="tabpanel" class="tab-pane" id="images">
                           <c:if test="${not empty legaldoc.getImages()}">
                                 <c:set var="imagecount" value="0"/>
                                <c:forEach var="image" items="${legaldoc.getImages()}" varStatus="loop">
                                    <c:set var="imagecount" value="${imagecount+1}"/>
                                    <a href="/resources/images/leg/${image}" data-lightbox="roadtrip"><img width="200" src="/resources/images/leg/${image}"</img></a>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty legaldoc.getImages()}">
                                <div class="alert alert-warning" role="alert"><spring:message code="basic.noimages"/></div>
                            </c:if>    
                        </div>       
                        <div role="tabpanel" class="tab-pane" id="problems">
                            <br/>
                        <c:if test="${not empty legaldoc.getIssues()}">
                            <c:forEach var="issue" items="${legaldoc.getIssues()}" varStatus="loop">
                                <div class="alert alert-danger" role="alert">${issue}</div>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty legaldoc.getIssues()}">
                            <div class="alert alert-warning" role="alert">Πιθανότατα δεν υπάρχουν προβλήματα</div>
                        </c:if>
                    </div>
                    </div>
                </div>
            </div>
            <div class="row"  style="height:400px;">&#160;&#160;</div>
        </div>
    </div>               
    <div id="footer" style="text-align: center; font-family:'Jura';" >
        <h5><spring:message code="footer"/> - Open Data&#160;&#160; <img src="/resources/images/rdf.png" width="15"/> </h5>
    </div>                        
<script src="/resources/js/lightbox.js"></script>
    <c:if test="${not empty legaldoc.getPlace()}">
        <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript" src="/resources/js/geoxml3-kmz.js"></script>
        <script type="text/javascript" src="/resources/js/ProjectedOverlay.js"></script>	

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