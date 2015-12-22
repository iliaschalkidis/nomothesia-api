<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:set var="years" value="${fn:split('2006,2007,2008,2009,2010,2011,2012,2013,2014,2015', ',')}" scope="application" />
<c:set var="types" value="${fn:split('ΣΥΝΟΛΟ,ΠΔ,Ν.,ΠΥΣ,ΥΑ,ΚΔ,ΠΝΠ', ',')}" scope="application" />

<!DOCTYPE html>
<html lang="en">
<head>
    
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="/resources/images/logo.png" >
    <title><spring:message code="title.stats"/></title>
    
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
    
    <c:set var="localeCode2" value="${pageContext.response.locale}"/>
            <script type="text/javascript">

                window.onload = function () {
                    var chart = new CanvasJS.Chart("chartContainer", {            
                        title:{
                            text: ""              
                        },

                        data: [  //array of dataSeries     
                        <c:set var="typecount" value="0"/>
                        <c:forEach var="list" items="${lists}" varStatus="loop">
                            { //dataSeries - first quarter
                                /*** Change type "column" to "bar", "area", "line" or "pie"***/        
                                type: "column",
                                name: "${types[typecount]}",
                                showInLegend: true,
                                dataPoints: [
                                <c:set var="yearcount" value="0"/>
                                <c:forEach var="num" items="${list}" varStatus="loop">
                                    { label: "${years[yearcount]}", y: ${num} },
                                    <c:set var="yearcount" value="${yearcount+1}"/>
                                </c:forEach>
                                ]
                            },
                            <c:set var="typecount" value="${typecount+1}"/>
                        </c:forEach>
                        ],
                        /** Set axisY properties here*/
                        axisY:{
                            prefix: "",
                            suffix: ""
                        }       
                    });

                    chart.render();
                
                    var chart2 = new CanvasJS.Chart("chartContainer2", {            
                        title:{
                            text: ""              
                        },

                        data: [  //array of dataSeries     
                            { //dataSeries - first quarter
                                /*** Change type "column" to "bar", "area", "line" or "pie"***/        
                                type: "column",
                                color: "#007bb6",
                                name: "ΠΝΠ",
                                showInLegend: true,
                                dataPoints: [
                                <c:set var="yearcount" value="0"/>
                                <c:forEach var="num" items="${lists.get(6)}" varStatus="loop">
                                    { label: "${years[yearcount]}", y: ${num} },
                                    <c:set var="yearcount" value="${yearcount+1}"/>
                                </c:forEach>
                                ]
                            }
                        ],
                        /** Set axisY properties here*/
                        axisY:{
                            prefix: "",
                            suffix: ""
                        }       
                    });

                    chart2.render();
                }  
            </script>
            
    <script type="text/javascript" src="/resources/js/canvasjs.min.js"></script>
  
  <style>
        #footer {
            position:relative;
            bottom:0;
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
      
    <!-- Stats -->
    <div class="container">
        <div class="row" style="min-height: 630px;">
            <div >
            <br/>
            <div class="alert alert-warning" role="alert"><spring:message code="stats.comment"/></div>
            <div class="row" style="padding:10px;">
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 25px;"><spring:message code="stats.header"/></div>        
            </div>
            <div class="row" style="padding:10px;">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 20px;"><spring:message code="stats.header2"/><br/></div>       
                <div id="chartContainer"></div>
            </div>
            </div>
        </div>
        <div class="row">
            <div>
              <div class="row" style="border-radius:5px; width: 100%">
           <div class="col-md-4" style="height: 20px; width: 40%; background-color: #005983"> 
              <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;">ΝΔ</div>
           </div>
           <div class="col-md-4" style="height: 20px; width: 20%; background-color: green;"> 
              <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;">ΠΑΣΟΚ</div>
           </div>
            <div class="col-md-4" style="height: 20px; width: 10%; background-color: #1ab7ea">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"> ΟΙΚ. (ΠΑΣΟΚ,ΝΔ,ΛΑΟΣ)</div>
            </div>
          <div  class="col-md-4" style="height:20px; width: 20%; background-color: #005983"> 
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"">ΝΔ</div>
          </div>
          <div  class="col-md-4" style="height:20px; width: 10%; background-color: #f7931e"> 
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"">ΣΥΡΙΖΑ</div>
          </div>
                  <br/>
                  <br/>
                  <br/>
          </div>
          </div>
        </div>
        <div class="row" style="min-height: 505px;">
            <div >
            <div class="row" style="padding:10px;">
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 20px;"><spring:message code="stats.header3"/><br/></div>
            <div id="chartContainer2"></div>
            </div>
            </div>
        </div>
        <div class="row">
            <div>
              <div class="row" style="border-radius:5px; width: 100%">
           <div class="col-md-4" style="height: 20px; width: 40%; background-color: #005983"> 
              <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;">ΝΔ</div>
           </div>
           <div class="col-md-4" style="height: 20px; width: 20%; background-color: green;"> 
              <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;">ΠΑΣΟΚ</div>
           </div>
            <div class="col-md-4" style="height: 20px; width: 10%; background-color: #1ab7ea">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"> ΟΙΚ. (ΠΑΣΟΚ,ΝΔ,ΛΑΟΣ)</div>
            </div>
          <div  class="col-md-4" style="height:20px; width: 20%; background-color: #005983"> 
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"">ΝΔ</div>
          </div>
          <div  class="col-md-4" style="height:20px; width: 10%; background-color: #f7931e"> 
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px; color: white;"">ΣΥΡΙΖΑ</div>
          </div>
          <br/>
            <br/>
            <br/>
            <br/>
            <div style="font-family: 'Comfortaa', cursive; text-align: justify; font-size: 15px;"><spring:message code="stats.comment3"/></div>

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
</body>
</html>