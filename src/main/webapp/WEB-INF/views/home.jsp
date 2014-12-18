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
    
    <!-- Για οταν δουλεψει το remote CSS-->
    <!-- <link href="/css/navbar.css" rel="stylesheet" type="text/css"/> -->
    
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
    
    <style>
        #custom-bootstrap-menu.navbar-default .navbar-brand {
            color: rgba(0, 0, 0, 1);
        }

        #custom-bootstrap-menu.navbar-default {
            font-size: 14px;
            background-color: rgba(214, 219, 227, 0.91);
            background: -webkit-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(214, 219, 227, 0.91) 100%);
            background: linear-gradient(to bottom, rgba(248, 248, 248, 1) 0%, rgba(214, 219, 227, 0.91) 100%);
            border-width: 0px;
            border-radius: 3px;
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-nav>li>a {
            color: rgba(18, 16, 16, 0.65);
            background-color: rgba(214, 219, 227, 1);
            background: -webkit-linear-gradient(top, rgba(248, 248, 248, 0) 0%, rgba(214, 219, 227, 1) 100%);
            background: linear-gradient(to bottom, rgba(248, 248, 248, 0) 0%, rgba(214, 219, 227, 1) 100%);
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-nav>li>a:hover,
        #custom-bootstrap-menu.navbar-default .navbar-nav>li>a:focus {
            color: rgba(23, 128, 214, 1);
            background-color: rgba(129, 138, 148, 0.47);
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-nav>.active>a,
        #custom-bootstrap-menu.navbar-default .navbar-nav>.active>a:hover,
        #custom-bootstrap-menu.navbar-default .navbar-nav>.active>a:focus {
            color: rgba(23, 128, 214, 1);
            background-color: rgba(129, 138, 148, 0.48);
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-toggle {
            border-color: #818a94;
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-toggle:hover,
        #custom-bootstrap-menu.navbar-default .navbar-toggle:focus {
            background-color: #818a94;
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-toggle .icon-bar {
            background-color: #818a94;
        }
        
        #custom-bootstrap-menu.navbar-default .navbar-toggle:hover .icon-bar,
        #custom-bootstrap-menu.navbar-default .navbar-toggle:focus .icon-bar {
            background-color: #d6dbe3;
        }
    </style>

</head>

<body>

    <div id="custom-bootstrap-menu" class="navbar navbar-default " role="navigation">
        <div class="container-fluid">
            <div class="navbar-header"><a class="navbar-brand"  href="#" style="font-family:'Jura'; font-size: 33px">ΝΟΜΟΘΕΣΙ@</a>
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
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
  </body>
</html>