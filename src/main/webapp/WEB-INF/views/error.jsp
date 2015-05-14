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
        <link href="/resources/css/navbar.css" rel="stylesheet"/>

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
                <div class="jumbotron" style="padding: 20px 20px 20px 20px;">
                    <h3><span class="glyphicon glyphicon-warning-sign"  aria-hidden="true"></span> <spring:message code="error1"/></h3><br/>
                    <spring:message code="error2"/>
                    <ul>
                        <li><spring:message code="error3"/></li>
                        <li><spring:message code="error4"/></li>
                        <li><spring:message code="error5"/></li>
                    </ul>
                    
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="row" style="padding:10px;">
                        <div class="jumbotron" style="padding: 20px 20px 20px 20px;">
                            <form role="form" method="GET" action="/search">
                                <div class="table-responsive">
                                <table width="100%">
                                    <tr>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><spring:message code="home.keywords"/></label>
                                                <input type="text" id="keywords" name="keywords" class="form-control" id="exampleInputEmail1" placeholder="<spring:message code="home.placeholder"/>">
                                            </div>
                                        </td>
                                        <td width="5%"></td>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputPassword1"><spring:message code="home.type"/></label>
                                                <select class="form-control" name="type">
                                                    <option value="">-</option>
                                                    <option value="con"><spring:message code="home.con"/></option>
                                                    <option value="law"><spring:message code="home.law"/></option>
                                                    <option value="pd"><spring:message code="home.pd"/></option>
                                                    <option value="amc"><spring:message code="home.amc"/></option>
                                                    <option value="md"><spring:message code="home.md"/></option>
                                                    <option value="rd"><spring:message code="home.rd"/></option>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td width="45%">
                                            <div class="form-group">
                                                <label for="exampleInputPassword2"><spring:message code="home.serial"/></label>
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
                                                <label for="exampleInputPassword2"><spring:message code="home.date"/></label>
                                                <div class='input-group date' >
                                                    <input type='text'  name="date" id='datepicker' class="form-control" placeholder="<spring:message code="home.dateplaceholder"/>"/>
                                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                </div> 
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td style="text-align: right;">
                                            <button type="submit" class="btn btn-primary btn-lg"><spring:message code="navbar.search"/></button>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td style="text-align: right; margin-top: 5px;">
                                            <a href="/search"><spring:message code="home.advancedsearch"/></a> 
                                        </td>
                                    </tr>
                                </table>
                                </div>
                                        
                                <input type="hidden" name="datefrom" value="" class="form-control"/>
                                <input type="hidden" name="dateto" value="" class="form-control"/>
                                <input type="hidden" name="fek_isuue" value="" class="form-control"/>
                                <input type="hidden" name="fek_year" value="" class="form-control"/>
                                <input type="hidden" name="fek_id" value="" class="form-control"/>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <img align="center" src="/resources/images/404.png"</img>
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

        <script>
            $(function() {
                $( "#datepicker" ).datepicker({
                    <c:set var="localeCode2" value="${pageContext.response.locale}" />
                <c:choose>
                    <c:when test="${localeCode2 == 'en' }">
                       dateFormat: 'yy-mm-dd', 
                    firstDay: 1,
                    maxDate: '0',
                    isRTL: false 
                    </c:when>
                    <c:when test="${localeCode2 == 'el_GR' }">
                        closeText: 'Κλείσιμο',
                    prevText: 'Προηγούμενος',
                    nextText: 'Επόμενος',
                    currentText: 'Τρέχων Μήνας',
                    monthNames: ['Ιανουάριος','Φεβρουάριος','Μάρτιος','Απρίλιος','Μάιος','Ιούνιος',
                    'Ιούλιος','Αύγουστος','Σεπτέμβριος','Οκτώβριος','Νοέμβριος','Δεκέμβριος'],
                    monthNamesShort: ['Ιαν','Φεβ','Μαρ','Απρ','Μαι','Ιουν',
                    'Ιουλ','Αυγ','Σεπ','Οκτ','Νοε','Δεκ'],
                    dayNames: ['Κυριακή','Δευτέρα','Τρίτη','Τετάρτη','Πέμπτη','Παρασκευή','Σάββατο'],
                    dayNamesShort: ['Κυρ','Δευ','Τρι','Τετ','Πεμ','Παρ','Σαβ'],
                    dayNamesMin: ['Κυ','Δε','Τρ','Τε','Πε','Πα','Σα'],
                    dateFormat: 'yy-mm-dd', 
                    firstDay: 1,
                    maxDate: '0',
                    isRTL: false
                    </c:when>
                    </c:choose>
                    

                });

            });
        </script>
      
        <script>
            $(document).ready(function() {
                
                $("#tabs").tabs( {
        "activate": function(event, ui) {
            $( $.fn.dataTable.tables( true ) ).DataTable().columns.adjust();
        }
    } );
                
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