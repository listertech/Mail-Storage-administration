<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CreateClient</title>
<link rel="stylesheet" type="text/css"
	href="resources/css/msacommon.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/font-awesome.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap-theme.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap-theme.min.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap.min.css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<!--script type="text/javascript" src="js/jquery-3.1.1.js"></script-->
<link rel="javascript" type="text/javascript"
	href="resources/js/bootstrap.js" />
<link rel="javascript" type="text/javascript" href="resources/js/npm.js" />
<script type="text/javascript" src="resources/js/msacommon.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#fault").hide();
		var eventhandler = function(e) {
			e.preventDefault();
		}
		$("#upd").click(function(e) {
			$("#myform").bind("submit", eventhandler);
			$("#uploadlogo").bind("submit", eventhandler);
			$("#uploadlogo").submit();
			$("#uploadlogo").unbind("submit", eventhandler);
			$("#myform").unbind("submit", eventhandler);
		});
		$("#sub").click(function(e) {
			$("#myform").bind("submit", eventhandler);
			var name = $("#name").val();
			console.log(name);
			var input = {};
			input["name"] = name;
			$.post("verifyClient", input, function(data, status) {
				console.log(data);
				//var info=JSON.parse(data);
				//console.log(info.result);
				if (data.result === false) {
					console.log("Form Submitted");
					$("#myform").unbind("submit", eventhandler);
					$("#myform").submit();
				} else {
					console.log("Cant submit form");
					$("#err").html("<label></label>Client already exists");
					$("#fault").show();
					$("#myform").unbind("submit", eventhandler);
				}
			});
		});
		$(".clientform").mouseover(function() {
			var id = $(this).attr('id');
			//console.log(id);
			formfocus(id);
		});
		$(".clientform").mouseout(function() {
			var id = $(this).attr('id');
			//console.log(id);
			formdefocus(id);
		});
		$("header ul li").mouseover(function() {
			var id = $(this).attr('id');
			//console.log(id);
			focus(id);
		});
		$("header ul li").mouseout(function() {
			var id = $(this).attr('id');
			console.log(id);
			defocus(id);
		});
	});
	function focus(id) {
		document.getElementById(id).style.boxShadow = "0px 8px 8px 4px";
		document.getElementById(id).style.backgroundColor = "#ff471a";
	}
	function defocus(id) {
		//console.log("#"+id);
		document.getElementById(id).style.boxShadow = "0px 0px 0px 0px";
		document.getElementById(id).style.backgroundColor = "#000000";
	}
	function formfocus(id) {
		document.getElementById(id).style.boxShadow = "0px 8px 8px 4px";
		//document.getElementById(id).style.backgroundColor = "#9ef8b2";
		document.getElementById(id).style.backgroundImage = "url('resources/images/bck.jpg')";
		document.getElementById(id).style.backgroundRepeat = "repeat";
	}
	function formdefocus(id) {
		//console.log("#"+id);
		document.getElementById(id).style.boxShadow = "0px 0px 0px 0px";
		//document.getElementById(id).style.backgroundColor = "#ffffff";
		document.getElementById(id).style.backgroundImage = "none";
		document.getElementById(id).style.backgroundRepeat = "no-repeat";
	}
</script>
<style>
.content-wrapper {
	height: 100%;
	width: 100%;
}

.clientform {
	margin-top: 5%;
	padding-top: 3.5%;
	padding-bottom: 3.5%;
	border-radius: 7px;
	width: 80%;
	height: 80%;
	margin-left: 10%;
	margin-bottom: 5%;
}

.form_body li:first-child {
	margin-top: 0%;
	padding-top: 2.5%;
}

.form_body li {
	padding-top: 10px;
	margin-top: 2.5%;
	list-style: none;
	height: 25px;
	color: #003399;
	font-size: 20px;
}

.form_body li label {
	width: 25%;
	color: black;
	font-weight: normal;
	font-family: Times New Roman;
}

.form_body li .error {
	color: red;
	font-size: 15px;
	padding-left: 3%;
}

.form_body #fault .err {
	color: red;
	font-size: 15px;
	padding-left: 3%;
}

.form_body li:last-child {
	margin-left: 40%;
	margin-top: 30px;
}

.field {
	position: absolute;
	margin-left: 2.5%;
	width: 60%;
	height: 30px;
	border-radius: 4px;
	box-shadow: 0 1px 1px 0.5px;
}
</style>
</head>
<body>
	<div class="wrapper">
		<header>
			<img id="oracle" class="oracle" src="resources/images/img3.png" /> <img
				id="logo" class="logo" src="resources/images/img1.png" />
			<div id="page_title">Mail Storage Administration</div>
			<ul>

				<li id="${ses.username}" style="display: inline-block; list-style: none">
					<div class="dropdown">
								<h4 class="dropdown-toggle"   data-toggle="dropdown">
									<span class="glyphicon glyphicon-user" style="color: white" aria-hidden="false"></span>&nbsp;&nbsp; ${ses.username}!
								</h4>
							    <ul class="dropdown-menu" style="width:5%">
							      <li><a href="logout">Logout</a></li>
							    </ul>
					</div>
			   </li>
				<li id="${ses.role}"
					style="display: inline-block; list-style: none; margin-left: 60%"><h4>
						<a href="homepage" class="glyphicon glyphicon-home" style="text-decoration:none;color: white"
							aria-hidden="true"></a>&nbsp;&nbsp;${ses.role}
					</h4></li>
			</ul>
		</header>
		<div class="content-wrapper">
			<div class="clientform" id="clientform">
				<div class="container">
					<div class="row">
						<div class="col-lg-9">
							<form:form id='myform' action="CreateClient" modelAttribute="client">
								<div class='form_body' id='form'>
									<ul>
										<li><label>Name</label> <form:input id="name" path="name"
												placeholder="                Enter Client's Name"
												onfocus="foc(this.id)" onblur="focout(this.id)"
												cssClass="field" /></li>
										<li id="fault"><div id="err" Class="err"
												style="color: red; font-size: 20px; padding-left: 3%"></div></li>
										<li><form:errors path="name" cssClass="error" /></li>
										<li><label>BCC Address</label> <form:input
												cssClass="field" id="bcc" path="bcc"
												placeholder="                Enter Client's BCC"
												onfocus="foc(this.id)" onblur="focout(this.id)" /></li>
										<li><label></label><form:errors path="bcc" cssClass="error" /></li>
										<li><label>Allocated Size</label> <form:select
												type="number" cssClass="field" id="allocated"
												path="allocated"
												placeholder="                Allocate Size in bytes"
												onfocus="foc(this.id);" onblur="focout(this.id)" >
												<option value="1024">1MB</option>
											    <option value="10240">10MB</option>
											    <option value="51200">50MB</option>
											    <option value="102400">100MB</option>
											    <option value="1048576">1GB</option>
												</form:select></li>
										<li><form:errors path="imagePath" cssClass="error" /></li>
										<li><label>Logo</label> <form:input cssClass="field"
												id="imagePath" path="imagePath"
												placeholder="                Enter logo URL"
												onfocus="foc(this.id)" onblur="focout(this.id)" /></li>
										<li><form:errors path="imagePath" cssClass="error" /></li>
										<li>
											<button class="btn btn-primary btn-lg" id="sub" type="submit">Create</button>
										</li>
									</ul>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>