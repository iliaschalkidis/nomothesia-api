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
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-3">
                <form role="form" method="GET" action="${pageContext.servletContext.contextPath}/legislation/search">
                <ul  class="nav nav-sidebar">
                    <li><a>Λέξεις Κλειδιά:</a>
                        <input type="text" name="keywords" class="form-control" id="keywords" placeholder="Εισάγετε επιθυμητή έκφραση...">
                    </li>
                    <li><a>Τύπος Νομοθεσίας:</a>
                        <fieldset>
                           <div class="checkbox">
                          <label><input type="checkbox" class="category" value="con">Σύνταγμα</label>
                           </div>
                           <div class="checkbox">
                          <label><input type="checkbox" class="category" value="law">Νόμος</label>
                           </div>
                           <div class="checkbox">
                          <label><input type="checkbox" class="category" value="pd">Προεδρικό Διάταγμα (ΠΔ)</label>
                           </div>
                           <div class="checkbox">
                          <label><input type="checkbox" class="category" value="amc">Πράξη Υπουργικού Συμβουλίου (ΠΥΣ)</label>
                           </div>
                           <div class="checkbox">
                          <label><input type="checkbox" class="category" value="md">Υπουργική Απόφαση (ΥΑ)</label>
                           </div>
                           <input id='categories' type='hidden' name='type' />
                        </fieldset>
                    </li>
                    <li><a>Αριθμός Κυκλοφορίας:</a>                        
                        <div class="row">
                        <div class="col-md-5">
                        <input class="form-control" value="" type="number" name="year" min="1976" max="2015" step="1">
                        </div>
                        <div class="col-md-1" style="font-size: 25px;">/</div>
                        <div class="col-md-5"> 
                        <input class="form-control" type="text" name="id" placeholder="N.">
                        </div>
                        </div>
                    </li>
                    <li><a>Αριθμός ΦΕΚ:</a>
                        <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                    <select class="form-control" name="fek_issue">
                                        <option value="A">Α'</option>
                                        <option value="B">Β'</option>
                                    </select>
                            </div>
                        </div>
                        <div class="col-md-4">
                        <input class="form-control" value="" type="number" name="fek_year" min="1976" max="2015" step="1">
                        </div>
                        <!--<div class="col-md-1" style="font-size: 25px;">/</div>-->
                        <div class="col-md-4"> 
                        <input class="form-control" type="text" name="fek_id" placeholder="N.">
                        </div>
                        </div>
                    </li>
                    <li><a>Ημερομηνία Κυκλοφορίας:</a>
                    <div class="form-group">                        
                        <div class='input-group date' >
                            <input type='text'  name="date" id='datepicker' class="form-control" placeholder="Ημερομηνία"/>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div> 
                    </div>
                    <div class="form-group">
                        <div class="row">
                        <div class="col-md-6">
                        <div class='input-group date' >
                            <input type='text'  name="datefrom" id='datepicker2' class="form-control" placeholder="Από"/>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                        </div>
                            <div class="col-md-6">
                        <div class='input-group date' >
                            <input type='text'  name="dateto" id='datepicker3' class="form-control" placeholder="Έως"/>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                        </div>
                        </div>
                    </div>
                    </li>
                    <li>
                   <button type="submit" class="btn btn-primary btn-lg">Αναζήτηση</button>
                    </li>
                </ul>
                </form>
            </div>
            <div class="col-md-9">
            <div style="font-family: 'Comfortaa', cursive; text-align: center; font-size: 25px;">ΑΠΟΤΕΛΕΣΜΑΤΑ ΑΝΑΖΗΤΗΣΗΣ</div>
            <br/><table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>Τίτλος</th>
                    <th>Κωδικός</th>
                    <th>Ημερομηνία</th>
                </tr>
                </thead>
                <tbody>
                     <c:forEach var="legaldoc" items="${legalDocuments}" varStatus="loop">
                    <tr>
                        <td><a href="${legaldoc.getURI()}" >${legaldoc.getTitle()}</a></td>
                        <td>${legaldoc.getDecisionType()} ${legaldoc.getYear()}/${legaldoc.getId()}</td>
                        <td>${legaldoc.getPublicationDate()}</td>
                    </tr>
                     </c:forEach>
                </tbody>
            </table>  
        </div>
        </div>
        <div class="row"  style="height:400px;">&#160;&#160;</div>
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
        $(document).ready(function() {
        var dis1 = document.getElementById("datepicker");
        dis1.onchange = function () {
           if (this.value != "" || this.value.length > 0) {
              document.getElementById("datepicker2").disabled = true;
              document.getElementById("datepicker3").disabled = true;
           }
           else{
              document.getElementById("datepicker2").disabled = false;
              document.getElementById("datepicker3").disabled = false;
           }
        }
    });
    </script>
    <script>
        $(document).ready(function() {
        var dis2 = document.getElementById("datepicker2");
        dis2.onchange = function () {
           if (this.value != "" || this.value.length > 0) {
              document.getElementById("datepicker").disabled = true;
           }
           else{
              document.getElementById("datepicker").disabled = false;
           }
        }
    });
    </script>
    <script>
        $(document).ready(function() {
        var dis3 = document.getElementById("datepicker3");
        dis3.onchange = function () {
           if (this.value != "" || this.value.length > 0) {
              document.getElementById("datepicker").disabled = true;
           }
           else{
              document.getElementById("datepicker").disabled = false;
           }
        }
    });
    </script>
    <script>
      $(function() {
        $( "#datepicker" ).datepicker(
                {
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
                isRTL: false}
                );
        
        $( "#datepicker2" ).datepicker(
                {
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
                isRTL: false}
                );
        
        $( "#datepicker3" ).datepicker(
                {
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
                isRTL: false}
                );
      });
      </script>
      <script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
                $('#example').dataTable({
       aaSorting: [[ 2, "desc" ]],
       bSortable: true,
      "iDisplayLength": 5,
       aoColumnDefs: [
            { "aTargets": [ 0 ], "bSortable": true },
            { "aTargets": [ 1 ], "bSortable": true },
            { "aTargets": [ 2 ], "bSortable": true }
        ],
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
  });
        } );
    </script>
    <!--<script>
            $(function(){
               $('html, body').animate({scrollTop:$('#${type1}-${id1}-${type2}-${id2}').position().top}, 'slow');
                return false;
            });
    </script>-->
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
    
    <script>
    $('form').submit(function() {
    var arr=[];

    $('input:checked[class=category]').each(function(){
        arr.push($(this).val());
    });

    $('#categories').val(arr.join(','));
    
    //alert($('#category').val());

    // Prevent actual submit for demo purposes:
    //return false;
    });
</script>

<script>
  $(function() {
    var availableTags = [
        <c:forEach var="tag" items="${tags}" >  
            "${tag}", 
        </c:forEach>
    ];
    $( "#keywords" ).autocomplete({
      source: availableTags
    });
  });
  </script>
      
</body>
</html>
