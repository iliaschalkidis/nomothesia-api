<%@page import="com.di.nomothesia.model.LegalDocument"%>
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
        <link rel="shortcut icon" href="/resources/images/logo.png" >
        <title><spring:message code="title.gazette"/></title>

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

        <!-- jQueryUI Calendar-->
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>  

        <script type="text/javascript">
        window.onload = function () {
                var chart = new CanvasJS.Chart("chartContainer", {
                        title:{
                               text: ""
                        },
                        axisX: {
                                tickThickness: 0,
                                interval: 1,
                                intervalType: "year"
                        },
                        animationEnabled: false,
                        toolTip: {
                                shared: false
                        },
                        axisY: {
                                lineThickness: 0,
                                tickThickness: 0,
                                interval: 20
                        },
                        data: [
                        {        
                                name: "ΦΕΚ τύπου 2",
                                showInLegend: true,
                                type: "stackedColumn100", 
                                color: "#004B8D ",
                                dataPoints: [
                                {x: new Date(2006,0), y: 188},
                                {x: new Date(2007,0), y: 209},
                                {x: new Date(2008,0), y: 196},
                                {x: new Date(2009,0), y: 177},
                                {x: new Date(2010,0), y: 181},
                                {x: new Date(2011,0), y: 213},
                                {x: new Date(2012,0), y: 185},
                                {x: new Date(2013,0), y: 203},
                                {x: new Date(2014,0), y: 207},
                                {x: new Date(2015,0), y: 0}
                                ]
                        }, 
                        {        
                                name: "ΦΕΚ τύπου 1",
                                showInLegend: true,
                                type: "stackedColumn100", 
                                color: "#0074D9 ",
                                dataPoints: [
                                {x: new Date(2006,0), y: 46},
                                {x: new Date(2007,0), y: 20},
                                {x: new Date(2008,0), y: 33},
                                {x: new Date(2009,0), y: 29},
                                {x: new Date(2010,0), y: 32},
                                {x: new Date(2011,0), y: 13},
                                {x: new Date(2012,0), y: 30},
                                {x: new Date(2013,0), y: 33},
                                {x: new Date(2014,0), y: 24},
                                {x: new Date(2015,0), y: 0}
                                ]
                        }, 
                        {        
                                name: "ΦΕΚ",
                                showInLegend: true,
                                type: "stackedColumn100", 
                                color: "#4192D9 ",
                                dataPoints: [
                                {x: new Date(2006,0), y: 50},
                                {x: new Date(2007,0), y: 66},
                                {x: new Date(2008,0), y: 37},
                                {x: new Date(2009,0), y: 33},
                                {x: new Date(2010,0), y: 35},
                                {x: new Date(2011,0), y: 46},
                                {x: new Date(2012,0), y: 42},
                                {x: new Date(2013,0), y: 52},
                                {x: new Date(2014,0), y: 50},
                                {x: new Date(2015,0), y: 0}
                                ]
                        }

                        ]
                });

        chart.render();
        }
        </script>
        <script type="text/javascript" src="/resources/js/canvasjs.min.js"></script>
        <style>
            #footer {
                position:relative;
                bottom: 0;
                width:100%;
                height:60px;   /* Height of the footer */
            }
        </style>    

    </head>
    <body>

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

        <!-- Tabs -->
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="row" style="padding:10px;">
                        <div role="tabpanel">
                            <!-- Nav tabs -->
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#introduction" aria-controls="home" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;"><spring:message code="gazette.full"/></a></li>
                                <li role="presentation"><a href="#background" aria-controls="home" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;"><spring:message code="gazette.year"/></a></li>

                            </ul>

                            <!-- Tab panes -->
                            <div class="tab-content" style="text-align:justify;">
                                <div role="tabpanel" style="text-align: center;" class="tab-pane fade in active" id="introduction">
                                    <br/>
                                    <div class="table-responsive">
                                    <br/><table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                                        <thead>
                                            <tr>
                                                <th><spring:message code="gazette.title"/></th>
                                                <th><spring:message code="gazette.date"/></th>
                                                <th><spring:message code="gazette.docs_num"/></th>
                                                <th><spring:message code="gazette.docs"/></th>
                                                <th><spring:message code="gazette.issues"/></th>
                                                <th><spring:message code="gazette.pdf"/></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="gaz" items="${gazs}" varStatus="loop">
                                                <tr>
                                                    <td>${gaz.getTitle()}</td>
                                                    <td>${gaz.getPublicationDate()}</td>
                                                    <td>${gaz.getDocs()}</td>
                                                    <td><c:choose><c:when test="${not empty gaz.getList()}"><c:forEach var="doc" items="${gaz.getList()}" varStatus="loop"><a href="${doc.getURI()}" target="_blank">${doc.getDecisionType()} ${doc.getId()}</a>,</c:forEach></c:when><c:when test="${empty gaz.getList()}">-</c:when></c:choose></td>
                                                    <td>${gaz.getIssues()}</td>
                                                    <td><a href="${gaz.getURI()}" target="_blank"><img src="/resources/images/pdf-icon.jpg" alt="PDF" /></a></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>  
                                    </div>
                                        
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="background">
                                    <br/>
                                    <div class="table-responsive">
                                    <br/><table id="example2" class="table table-striped table-bordered" cellspacing="0" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Έτος</th>
                                                <th>Κατηγορία ΦΕΚ 1*</th>
                                                <th>Κατηγορία ΦΕΚ 2**</th>
                                                <th>Αριθμός ΦΕΚ</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td>2006</td><td>188 (<c:out value="${fn:substringBefore(18800/284,'.')}"/>%)</td><td>234 (<c:out value="${fn:substringBefore(23400/284,'.')}"/>%)</td><td>284</td></tr>
                                            <tr><td>2007</td><td>209 (<c:out value="${fn:substringBefore(20900/295,'.')}"/>%)</td><td>229 (<c:out value="${fn:substringBefore(22900/295,'.')}"/>%)</td><td>295</td></tr>
                                            <tr><td>2008</td><td>196 (<c:out value="${fn:substringBefore(19600/266,'.')}"/>%)</td><td>229 (<c:out value="${fn:substringBefore(22900/266,'.')}"/>%)</td><td>266</td></tr>
                                            <tr><td>2009</td><td>177 (<c:out value="${fn:substringBefore(17700/239,'.')}"/>%)</td><td>206 (<c:out value="${fn:substringBefore(20600/239,'.')}"/>%)</td><td>239</td></tr>
                                            <tr><td>2010</td><td>181 (<c:out value="${fn:substringBefore(18100/248,'.')}"/>%)</td><td>213 (<c:out value="${fn:substringBefore(21300/248,'.')}"/>%)</td><td>248</td></tr>
                                            <tr><td>2011</td><td>213 (<c:out value="${fn:substringBefore(21300/272,'.')}"/>%)</td><td>226 (<c:out value="${fn:substringBefore(22600/272,'.')}"/>%)</td><td>272</td></tr>
                                            <tr><td>2012</td><td>185 (<c:out value="${fn:substringBefore(18500/257,'.')}"/>%)</td><td>215 (<c:out value="${fn:substringBefore(21500/257,'.')}"/>%)</td><td>257</td></tr>
                                            <tr><td>2013</td><td>203 (<c:out value="${fn:substringBefore(20300/288,'.')}"/>%)</td><td>236 (<c:out value="${fn:substringBefore(23600/288,'.')}"/>%)</td><td>288</td></tr>
                                            <tr><td>2014</td><td>207 (<c:out value="${fn:substringBefore(20700/281,'.')}"/>%)</td><td>231 (<c:out value="${fn:substringBefore(23100/281,'.')}"/>%)</td><td>281</td></tr>
                                            <tr><td>ΣΥΝΟΛΟ</td><td>1759 (<c:out value="${fn:substringBefore(175900/2430,'.')}"/>%)</td><td>2019 (<c:out value="${fn:substringBefore(201900/2430,'.')}"/>%)</td><td>2430</td></tr>
                                        </tbody>
                                    </table>  
                                    </div>
                                     <br/>
                                    <br/>
                                    <div  class="row" >
                                        <div  class="row">
                                    <div id="chartContainer" style="height: 300px; width: 100%;">
                                    </div></div>
                                    <br/>
                                    <div  class="row" style="font-family: 'Comfortaa', cursive; text-align: justify; font-size: 15px;">*Οι ΠΝΠ (Πράξεις Νομοθετικού Περιεχομένου) δεν χρειάζονται άμεση έγκριση μέσω ψηφοφορίας από την Βουλή των Ελλήνων.</div>
                                    <div  class="row" style="font-family: 'Comfortaa', cursive; text-align: justify; font-size: 15px;">**Οι ΠΝΠ (Πράξεις Νομοθετικού Περιεχομένου) δεν χρειάζονται άμεση έγκριση μέσω ψηφοφορίας από την Βουλή των Ελλήνων.</div>
                                    </div>
                                    </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="footer" style="text-align: center; font-family:'Jura';" >
            <h5><spring:message code="footer"/> - Open Data&#160;&#160; <img src="/resources/images/rdf.png" width="15"/> </h5>
        </div>
        
        

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.js"></script>
        
        <script type="text/javascript" charset="utf-8">
                    $(document).ready(function() {
            $('#example').dataTable({
            aaSorting: [[ 2, "desc" ]],
                    bSortable: true,
                    "iDisplayLength": 10,
                    aoColumnDefs: [
                    { "aTargets": [ 0 ], "bSortable": true },
                    { "aTargets": [ 1 ], "bSortable": true },
                    { "aTargets": [ 2 ], "bSortable": false }
                    ],
                    "columns": [
                    { "width": "70%" },
                    { "width": "20%" },
                    { "width": "10%" }
                   ],
            <c:set var="localeCode2" value="${pageContext.response.locale}" />
            <c:choose>
                <c:when test="${localeCode2 == 'en' }">

            "bLengthChange": false
                </c:when>
                <c:when test="${localeCode2 == 'el_GR' }">
            "bLengthChange": false,
                    "oLanguage": {
                    "sLengthMenu": "Εμφάνισε _MENU_ εγγραφές ",
                            "sZeroRecords": "Δεν βρέθηκε τίποτα",
                            "sInfo": "Εμφανίζει από _START_ μέχρι _END_ των _TOTAL_ εγγραφών",
                            "sInfoEmpty": "Εμφανίζει 0 εγγραφές",
                            "sInfoFiltered": "(φιλτραρισμένες _MAX_ συνολικά εγγραφές)",
                            "sSearch": "Αναζήτηση",
                            "oPaginate": {
                            "sNext": "Επόμενη",
                                    "sPrevious": "Προηγούμενη"
                            }
                    }
                </c:when>
            </c:choose>
            });
            });        
        </script>
        
        <script type="text/javascript" charset="utf-8">
                    var is_profile_inited = false;
            $(document).on('shown.bs.tab', '[data-toggle="tab"]', function (e) {
                if ($(this).attr("href") == '#background' && is_profile_inited == false) {
                    is_profile_inited = true;
                    $("#example2").dataTable({
            aaSorting: [[ 2, "desc" ]],
                    bSortable: true,
                    "iDisplayLength": 10,
                    aoColumnDefs: [
                    { "aTargets": [ 0 ], "bSortable": true },
                    { "aTargets": [ 1 ], "bSortable": true },
                    { "aTargets": [ 2 ], "bSortable": false }
                    ],
                    "columns": [
                    { "width": "70%" },
                    { "width": "20%" },
                    { "width": "10%" }
                   ],
            <c:set var="localeCode2" value="${pageContext.response.locale}" />
            <c:choose>
                <c:when test="${localeCode2 == 'en' }">

            "bLengthChange": false
                </c:when>
                <c:when test="${localeCode2 == 'el_GR' }">
            "bLengthChange": false,
                    "oLanguage": {
                    "sLengthMenu": "Εμφάνισε _MENU_ εγγραφές ",
                            "sZeroRecords": "Δεν βρέθηκε τίποτα",
                            "sInfo": "Εμφανίζει από _START_ μέχρι _END_ των _TOTAL_ εγγραφών",
                            "sInfoEmpty": "Εμφανίζει 0 εγγραφές",
                            "sInfoFiltered": "(φιλτραρισμένες _MAX_ συνολικά εγγραφές)",
                            "sSearch": "Αναζήτηση",
                            "oPaginate": {
                            "sNext": "Επόμενη",
                                    "sPrevious": "Προηγούμενη"
                            }
                    }
                </c:when>
            </c:choose>
            });
            });        
        </script>

    </body>
</html>