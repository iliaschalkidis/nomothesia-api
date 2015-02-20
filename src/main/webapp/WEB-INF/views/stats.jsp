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
    
      <script type="text/javascript">
  window.onload = function () {
    var chart = new CanvasJS.Chart("chartContainer", {            
      title:{
        text: ""              
      },

      data: [  //array of dataSeries     
      { //dataSeries - first quarter
 /*** Change type "column" to "bar", "area", "line" or "pie"***/        
       type: "column",
       name: "ΠΔ",
       showInLegend: true,
       dataPoints: [
       { label: "2008", y: 200 },
       { label: "2009", y: 194 },
       { label: "2010", y: 152 },
       { label: "2011", y: 138 },
       { label: "2012", y: 136 },                                    
       { label: "2013", y: 177 },
       { label: "2014", y: 178 }
       ]
     },

     { //dataSeries - second quarter
      color: "#B0D0B0",
      type: "column",
      name: "Νόμος", 
      showInLegend: true,               
      dataPoints: [
      { label: "2008", y: 3732 },
      { label: "2009", y: 3813 },
      { label: "2010", y: 3906 },
      { label: "2011", y: 4035 },
      { label: "2012", y: 4102 },                                    
      { label: "2013", y: 4224 },
      { label: "2014", y: 4319 }
      ]
    }
    ],
 /** Set axisY properties here*/
    axisY:{
      prefix: "",
      suffix: ""
    }    
  });

chart.render();

    var chart2 = new CanvasJS.Chart("chartContainer2",
    {
      title:{
        text: ""              
      },
      legend:{
        verticalAlign: "bottom",
        horizontalAlign: "center"
      },
      data: [
      {
        //startAngle: 45,
       //indexLabelFontSize: 20,
       //indexLabelFontFamily: "Garamond",
       //indexLabelFontColor: "darkgrey",
       //indexLabelLineColor: "darkgrey",
       //indexLabelPlacement: "outside",
       type: "doughnut",
       showInLegend: true,
       dataPoints: [
       {  y: 42.37, legendText:"ΥΠΟΙΚ 2123"},
       {  y: 21.0, legendText:"ΥΠΕΣ 1232"},
       {  y: 12, legendText:"ΥΠΕΧΩΔΕ 567"},
       {  y: 8.2, legendText:"ΥΠΥ 452"},
       {  y: 7.3, legendText:"ΥΠΔΜΕ 389"},
       {  y: 4.2, legendText:"ΥΠΠΟΛ 178"},
       {  y: 2.1, legendText:"ΥΠΕΞ 83"},
       {  y: 1.0, legendText:"ΛΟΙΠΑ 1%"}
       ]
     }
     ]
   });

    chart2.render();
  }
  </script>
  <script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/canvasjs.min.js"></script>
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
      
    <!-- Stats -->
    <div class="container">
        <div class="row" style="min-height: 505px;">
            <div class="col-md-6">
            <div class="row" style="padding:10px;">
                <div id="chartContainer" style="height: 300px; width: 400px;"></div>
            </div>
            </div>
            <div class="col-md-6">
           <div class="row" style="padding:10px;">
               <div id="chartContainer2" style="height: 300px; width: 300px;"></div>
            </div>  
                    
            </div>
        </div>
        <div class="row" style="margin:10px; text-align: center; font-family:'Jura';">
            <h5>Νομοθεσί@ &copy; 2014 - Τμήμα Πληροφορικής &amp; Τηλ/νωνιών ΕΚΠΑ - Open Data&#160;&#160; <img src="${pageContext.servletContext.contextPath}/resources/images/rdf.png" width="15"/> </h5>
        </div>
    </div>
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
</body>
</html>