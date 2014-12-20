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
    <div class="container">
        <div class="row">
            <div class="col-md-6">
            <div class="row" style="padding:10px;">
            <div class="jumbotron" style="padding: 20px 20px 20px 20px;">
                <form role="form">
                    <table width="100%">
                        <tr >
                            <td width="45%">
                                <div class="form-group">
                                    <label for="exampleInputEmail1">Λέξεις Κλειδιά:</label>
                                    <input type="text" class="form-control" id="exampleInputEmail1" placeholder="Εισάγετε επιθυμητή έκφραση...">
                                </div>
                            </td>
                            <td width="5%"></td>
                            <td width="45%">
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
                            </td>
                        </tr>
                        <tr>
                            <td width="45%">
                                <div class="form-group">
                                    <label for="exampleInputPassword2">Αριθμός Κυκλοφορίας:</label>
                                    <div class="row">
                                    <div class="col-md-5">
                                    <input class="form-control" value="2014" type="number" name="year" min="1976" max="2015" step="1">
                                    </div>
                                    <div class="col-md-1" style="font-size: 25px;">/</div>
                                    <div class="col-md-5"> 
                                    <input class="form-control" type="text" name="id" placeholder="N.">
                                    </div>
                                </div>
                            </td>
                            <td width="5%"></td>
                            <td width="45%">
                                <div class="form-group">
                                    <label for="exampleInputPassword2">Ημερομηνία Κυκλοφορίας:</label>
                                    <div class='input-group date' >
                                        <input type='text'  id='datepicker' class="form-control" />
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div> 
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                               
                            </td>
                            <td></td>
                            <td style="text-align: right;">
                                    <button type="submit" class="btn btn-primary btn-lg">Αναζήτηση</button>
                                    
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td style="text-align: right; margin-top: 5px;">
                                <a href="${pageContext.servletContext.contextPath}/search">Σύνθετη Αναζήτηση</a 
                            </td>
                            
                        </tr>
                    </table>    
                </form>
            </div>
            </div>
            <div class="row" style="padding:10px;">
                <div role="tabpanel">

                  <!-- Nav tabs -->
                  <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">ΝΕΕΣ ΕΚΔΟΣΕΙΣ</a></li>
                    <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab" style="font-family: 'Comfortaa', cursive;">ΠΙΟ ΔΗΜΟΦΙΛΗ</a></li>

                  </ul>

                  <!-- Tab panes -->
                  <div class="tab-content">
                      <div role="tabpanel" class="tab-pane active" id="home">
                          <br/>
                          <table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Τίτλος</th>
                                <th>Κωδικός</th>
                                <th>Ημερομηνία</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><a href="pd/2013/40" >Ηλεκτρονική κατάθεση δικογράφων, ηλεκτρονική χορήγηση σχετικών πιστοποιητικών και λοιπών εγγράφων στο Συμβούλιο της Επικρατείας και στα Τακτικά Διοικητικά Δικαστήρια</a></td>
                                <td>ΠΔ (ΠΡΟΕΔΡΙΚΟ ΔΙΑΤΑΓΜΑ) 2013/40</td>
                                <td>2013-03-22</td>
                            </tr>
                        </tbody>
                        </table>   
                      </div>
                    <div role="tabpanel" class="tab-pane" id="profile">
                        <br/>
                        <table id="example2" class="table table-striped table-bordered" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Τίτλος</th>
                                <th>Κωδικός</th>
                                <th>Ημερομηνία</th>
                            </tr>
                        </thead>
                        <tbody>
                             <tr>
                                <td><a href="pd/2011/54" >Σύσταση Ειδικής Υπηρεσίας Δημοσίων Έργων Αντιπλημμυρικής προστασίας της Κοιλάδας του ποταμού Έβρου και των παραποτάμων του (ΕΥΔΕ ΕΒΡΟΥ)</a></td>
                                <td>ΠΔ (ΠΡΟΕΔΡΙΚΟ ΔΙΑΤΑΓΜΑ) 2011/54</td>
                                <td>2011-05-20</td>
                            </tr>
                            <tr>
                                <td><a href="pd/2012/10" >Τροποποίηση του υπ’ αριθμ. 54/2011 Προεδρικού Διατάγματος «Σύσταση Ειδικής Υπηρεσίας Δημοσίων Έργων Αντιπλημμυρικής Προστασίας της Κοιλάδας του ποταμού Έβρου και των παραποτάμων του (ΕΥΔΕ ΕΒΡΟΥ)» (Α΄ 135)</a></td>
                                <td>ΠΔ (ΠΡΟΕΔΡΙΚΟ ΔΙΑΤΑΓΜΑ) 2012/10</td>
                                <td>2012-01-30</td>
                            </tr>
                            <tr>
                                <td><a href="pd/2013/40" >Ηλεκτρονική κατάθεση δικογράφων, ηλεκτρονική χορήγηση σχετικών πιστοποιητικών και λοιπών εγγράφων στο Συμβούλιο της Επικρατείας και στα Τακτικά Διοικητικά Δικαστήρια</a></td>
                                <td>ΠΔ (ΠΡΟΕΔΡΙΚΟ ΔΙΑΤΑΓΜΑ) 2013/40</td>
                                <td>2013-03-22</td>
                            </tr>
                        </tbody>
                        </table>   
                    </div>
                  </div>
                </div>
            </div>
            </div>
                <div class="col-md-6">
                    
                    <div style="text-align: justify;padding:10px;">
                    <p><img style="width:40%; margin-left: 10px; margin-bottom: 10px;" align="right" src="${pageContext.servletContext.contextPath}/resources/images/greekcloud.png"</img>
                        Στις μέρες μας, σε αντίθεση με πολλές χώρες της Ευρωπαϊκής Ένωσης, δεν υπάρχει κάποιο
                        μηχανικά αναγνώσιμο πρότυπο για την ελληνική νομοθεσία και επομένως δεν μπορούν να
                        προκύψουν προηγμένες διαδικτυακές νομικές υπηρεσίες. Στόχος μας είναι να συνισφέρουμε
                        στον τομέα της αναπαράστασης νομικής γνώσης και στην ενσωμάτωση αυτής στην περιοχή
                        των ανοιχτών δεδομένων στην Ελλάδα, τόσο από τεχνολογική σκοπιά όσο και από άποψη
                        διαφάνειας. Η συγκεκριμένη εργασία παρουσιάζει την Νομοθεσί@, μια πλατφόρμα, που
                        σκοπό έχει να δώσει πρόβαση στην ελληνική νομοθεσία, με την χρήση ενός νομικού XML
                        προτύπου και τα διασυνδεδεμένα δεδομένα (linked data). Η Νομοθεσί@ προτείνει ένα νεό
                        XML πρότυπο για την αναπαράσταση τον νομοθετικών εγγράφων και ταυτόχρονα υιοθετεί
                        την OWL οντολογία CEN Metalex, παράλληλα με άλλες, με σκοπό να περιγράψει τις νομοθετικές 
                        σχέσεις και γεγονότα. Η σύνδεση δεδομένων αφορά την διασύνδεση και ανοιχτή δημοσίευση 
                        ελληνικών δημόσιων δεδομένων και των νομοθετικών δεδομένων κατά μήκος της Ευρωπαϊκή Ένωσης,
                        με σκοπό την ενίσχυση της ηλεκτρονικής διακυβέρνησης. Πάνω σε αυτές
                        τις αρχές, χτίζουμε ένα Σύστημα Διαχείρισης Περιεχομένου (ΣΔΠ) για την διαχείρηση του
                        νομοθετικού αρχείου και διαδικτυακές υπηρεσίες με σκοπό την αναζήτηση και παρουσίαση
                        της ελληνικής νομοθεσίας.
                    </p>
                    <div align="center" style="padding:10px;">
                        <a class="btn btn-primary btn-lg" href="downloadPDF" style="width:100%"><span class="glyphicon glyphicon-save" aria-hidden="true"></span> Download OWL Ontology</a>
                    </div>
                    <div align="center" style="padding:10px;">
                        <a class="btn btn-primary btn-lg" href="downloadPDF" style="width:100%"><span class="glyphicon glyphicon-save" aria-hidden="true"></span> Download RDF Data Set</a>
                    </div>
                    </div>
                </div>
        </div>
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
      });
      </script>
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
   <script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
                $('#example').dataTable({
       aaSorting: [[ 2, "desc" ]],
       bSortable: true,
      "iDisplayLength": 4,
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
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
                $('#example2').dataTable({
       aaSorting: [[ 2, "desc" ]],
       bSortable: true,
      "iDisplayLength": 4,
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
</body>
</html>