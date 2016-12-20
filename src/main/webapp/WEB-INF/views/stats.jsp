<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:message code="stats.years" var="allYears"/>
<c:set var="years" value="${fn:split(allYears, ',')}" scope="application"/>
<spring:message code="stats.types" var="allTypes"/>
<c:set var="types" value="${fn:split(allTypes, ',')}" scope="application"/>

<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="/resources/images/logo.png">
    <title><spring:message code="title.stats"/></title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
    <link href='http://fonts.googleapis.com/css?family=Jura&subset=latin,greek' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Comfortaa&subset=latin,greek' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/3/dataTables.bootstrap.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Load CSS -->
    <link href="/resources/css/navbar.css" rel="stylesheet"/>

    <!-- jQueryUI Calendar-->
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet"
          type="text/css"/>

    <c:set var="localeCode2" value="${pageContext.response.locale}"/>
    <script type="text/javascript">

        window.onload = function () {
            var chart = new CanvasJS.Chart("chartContainer", {
                title: {
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
                            {label: "${years[yearcount]}", y: ${num}},
                            <c:set var="yearcount" value="${yearcount+1}"/>
                            </c:forEach>
                        ]
                    },
                    <c:set var="typecount" value="${typecount+1}"/>
                    </c:forEach>
                ],
                /** Set axisY properties here*/
                axisY: {
                    prefix: "",
                    suffix: ""
                }
            });

            chart.render();

            var chart2 = new CanvasJS.Chart("chartContainer2", {
                title: {
                    text: ""
                },

                data: [  //array of dataSeries
                    { //dataSeries - first quarter
                        /*** Change type "column" to "bar", "area", "line" or "pie"***/
                        type: "column",
                        color: "#007bb6",
                        name: "<spring:message code='stats.la' javaScriptEscape='true'/>",
                        showInLegend: true,
                        dataPoints: [
                            <c:set var="yearcount" value="0"/>
                            <c:forEach var="num" items="${lists.get(6)}" varStatus="loop">
                            {label: "${years[yearcount]}", y: ${num}},
                            <c:set var="yearcount" value="${yearcount+1}"/>
                            </c:forEach>
                        ]
                    }
                ],
                /** Set axisY properties here*/
                axisY: {
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
            position: relative;
            bottom: 0;
            width: 100%;
            height: 60px; /* Height of the footer */
        }
    </style>
</head>

<body>

<!-- Include Navbar -->
<%@ include file="/resources/html/navbar.html" %>

<!-- Stats Page -->
<div class="container">
    <div class="row" style="min-height: 630px;">
        <div>
            <br/>
            <div class="alert alert-warning" role="alert"><strong><spring:message code="stats.comment"/></strong></div>
            <div class="row" style="padding:10px;">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 25px;"><spring:message
                        code="stats.header"/></div>
            </div>
            <div class="row" style="padding:10px;">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 20px;"><spring:message
                        code="stats.header2"/><br/></div>
                <div id="chartContainer"></div>
            </div>
        </div>
    </div>

    <!-- Include Stats Bar -->
    <%@ include file="/resources/html/statsbar.html" %>

    <div class="row" style="min-height: 505px;">
        <div>
            <div class="row" style="padding:10px;">
                <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 20px;"><spring:message
                        code="stats.header3"/><br/></div>
                <div id="chartContainer2"></div>
            </div>
        </div>
    </div>

    <!-- Include Stats Bar -->
    <%@ include file="/resources/html/statsbar.html" %>

    <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 15px;"><spring:message
            code="stats.comment3"/></div>
</div>

<!-- Include Footer -->
<%@ include file="/resources/html/footer.html" %>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
</body>
</html>