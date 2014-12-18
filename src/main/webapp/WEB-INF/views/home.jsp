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
    <link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/resources/images/zigaria.png" >
    <title>NOMOΘΕΣΙΑ</title>
    
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
    <link href='http://fonts.googleapis.com/css?family=Jura&subset=latin,greek' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Comfortaa&subset=latin,greek' rel='stylesheet' type='text/css'>
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
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>  
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script> 
    <script>
        $(function(){
            $.datepicker.setDefaults(
	    $.extend($.datepicker.regional['']));
            $('#datepicker').datepicker();
        });
    </script>
    
</head>

<body>
    
    <!-- Navigation Bar -->
    <div id="custom-bootstrap-menu" class="navbar navbar-default " role="navigation">
        <div class="container-fluid">
            <div class="navbar-header"><a class="navbar-brand"  href="#"><img style="width: 34px; height: 50px; margin-top: -10px;" src="${pageContext.servletContext.contextPath}/resources/images/ekpa.png"</img></a>
            <a class="navbar-brand"  href="#" style="font-family:'Jura'; font-size: 33px">ΝΟΜΟΘΕΣΙ@</a>
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
                        <a href="#" style="font-family: 'Comfortaa', cursive;">Αρχική</a>
                    </li>
                    <li>
                        <a href="#" style="font-family: 'Comfortaa', cursive;" >Πληροφορίες</a>
                    </li>
                    <li>
                        <a href="#" style="font-family: 'Comfortaa', cursive;" >Πληροφορίες</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
      
    <!-- Search Form -->
    <div class="container">
        <div class="row">
            <div class="jumbotron">
                <h3>Αναζήτηση</h3>
                <form role="form">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Λέξεις Κλειδιά:</label>
                        <input type="text" class="form-control" id="exampleInputEmail1" placeholder="Εισάγετε επιθυμητή έκφραση...">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">Τύπος Νομοθεσίας:</label>
                        <select class="form-control" name="leg_type">
                            <option value="1">Σύνταγμα</option>
                            <option value="2">Νόμος</option>
                            <option value="3">Προεδρικό Διάταγμα (ΠΔ)</option>
                            <option value="4">Πράξη Υπουργικού Συμβουλίου (ΠΥΣ)</option>
                            <option value="5">Υπουργική Απόφαση (ΥΑ)</option>
                        </select>
                    </div>
                
                    <div class="form-group">
                        <label for="exampleInputPassword2">Αριθμός Κυκλοφορίας:</label>
                        <div class="navbar-form">
                            <input  style="width: 85px; height:32px" value="2014" type="number" name="year" min="1976" max="2015" step="1"> 
                            <input style="height:32px; width: 85px;" type="text" name="id">
                        </div>
                    </div>
                
                    <div class="form-group">
                        <label for="exampleInputPassword2">Ημερομηνία Κυκλοφορίας:</label>
                        <input class="form-control" type="text" id="datepicker" size="30"/>   
                    </div>
                
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Αυτο αστο να υαπρχει γιανα μπρω να παταω το κουμπι να τρεχει το pdf να τσεκαρω τις αλλαγες -->        
    <div align="center">
        <h1>Prokat nomos gia download</h1>
	<h3><a href="downloadPDF">Download PDF Document</a></h3>
    </div> 
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

</body>
</html>