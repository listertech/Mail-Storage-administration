<%-- 
    Document   : home
    Created on : Nov 17, 2016, 6:05:49 PM
    Author     : souvik.p
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <link rel="stylesheet" type="text/css" href="resources/css/msa.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap-theme.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <!--script type="text/javascript" src="js/jquery-3.1.1.js"></script-->
        <link rel="javascript" type="text/javascript" href="resources/js/bootstrap.js"/>
        <link rel="javascript" type="text/javascript" href="resources/js/npm.js"/>
        <script type="text/javascript" src="resources/js/msa.js"></script>
        <style type="text/css">
        	.form_body li .error{
				color:red;
				font-size:15px;
				padding-left:20px;
			}
			#login_form{
			height:450px;
			}
        </style>
    </head>
    <body style="background-image:url('resources/images/img2.jpg')">
        <div class="wrapper" id='wrap'>
            <img id="oracle" src="resources/images/img3.png"/>
            <img id="logo" src="resources/images/img1.png"/>
            <header>
                <div id="page_title">
                    Mail Storage Administration
                </div>
            </header>
            <div class="container" id="login_block">
                <div class="row">
                    <div class="col-lg-8 col-lg-offset-3"  id="login_form">
                        <form:form id='myform' action="login" modelAttribute="user">
                            <div class='form_body' id='form'>
                                <ul>
                                <li><h1>${greeting}</h1></li>    
                                <li>Username   
                                    <form:input id="username" path="username" placeholder="        Enter your Username" onfocus="foc(this.id)" onblur="focout(this.id)" cssClass="field" />
                                    
                                </li>
                                <li>
                                	<form:errors path="username" cssClass="error"/>
                                </li>
                                <li>Password&nbsp;<form:password cssClass="field" id="password" path="password" placeholder="        Enter your password" onfocus="foc(this.id)" onblur="focout(this.id)"/>
                                </li>
                                <li>
                                	<form:errors path="password" cssClass="error"/>
                                </li>
                                <li>
                                    <button class="btn btn-primary btn-lg" id="sub"  type="submit" >Sign in</button>
                                </li>
                                </ul>
                             </div>   
                        </form:form>     
                    </div>
            </div>
        </div>
        </div>
    </body>
</html>
