
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success</title>
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

<style>
.content-wrapper {
	height: 100%;
	width: 100%;
}

.clientdtls {
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
.form_body li:nth-child(2) {
	margin-left: 37.5%;
}
.form_body li:last-child {
	margin-left: 37.5%;
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
			<div class="clientdtls" id="clientdtls">
				<div class="container">
					<div class="row">
						<div class="col-lg-9">
							<h2>Congrats! Client ${client.name} has been created</h2>
							<div class="form_body">
								<ul>
									<li><img src="${client.imagePath}" style="height:100px;width:100px"/></li>
									<li><label>Name:</label>&nbsp;<label style="color:blue">${client.name}</label></li>
									<li><label>BCC Address:</label>&nbsp;<label style="color:blue">${client.bcc}</label></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>