<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage</title>
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

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script>
	var home="<%=(String)request.getAttribute("currentdir")%>";
	var currentdir=home;
	var fname="";
	$(document).ready(function(){
		$(".newfolder").hide();
		var eventhandler = function(e) {
			e.preventDefault();
		}
		function browse(id){
			fname=id;
			$(".content-wrapper").hide();
			currentdir=currentdir+"/"+fname;
			//alert(currentdir);
			var text={};
			text["currentdir"]=currentdir;
			//alert(JSON.stringify(text));
			$.post("browsefolders",text, function(data, status) {
				console.log(data);
				var cover="<section class='container'><div class='row'>";
				var str=""
				for(var i=0;i<data.folders.length;i++){
					str=str+"<div class='col-sm-4 col-md-2' style='padding-bottom: 15px; height: 300px'> <p><a href='#' id='browse' rel='"+data.folders[i]+"'><span class='glyphicon glyphicon-folder-open' style='text-decoration:none;color:#ffd11a;font-size:100px' aria-hidden='true'></span></a></p> <p id='fname' style='text-decoration:none;font-size:22.5px;padding-left:2%'>"+data.folders[i]+"</p><p><button id='rem/"+data.folders[i]+"' onclick='remove(this.id)' class='btn btn-danger btn-md'><span class='glyphicon glyphicon-remove' style='text-decoration:none;color:white' aria-hidden='true'></span>&nbsp;Remove</button></p></div>";
					//alert(str);	
				}
				cover=cover+str+"</div></section>";
				$(".content-wrapper").html(cover);
				str="";
				cover="";
				$(".content-wrapper").show();
				if(currentdir===home){
					$(".bck").hide();
					$(".new").css("margin-top","2%");
				}
				else{
					$(".bck").show();
					$(".new").css("margin-top","-2.125%");
				}
			});
		}
		$(document).on("click","#back",function(){
			$(".content-wrapper").hide();
			var st=[];
			st=currentdir.split('/');
			var newdir="";
			for(var i=0;i<st.length-1;i++){
				newdir=newdir+st[i];
				if(i!==st.length-2){
					newdir=newdir+"/";
				}
			}
			currentdir=newdir;
			console.log("back to"+currentdir);
			var txt={};
			txt["currentdir"]=currentdir;
			$.post("browsefolders",txt, function(data, status) {
				console.log(data);
				var cover="<section class='container'><div class='row'>";
				var str=""
				for(var i=0;i<data.folders.length;i++){
					str=str+"<div class='col-sm-4 col-md-2' style='padding-bottom: 15px; height: 300px'> <p><a href='javascript:void(0)'  id='"+data.folders[i]+"' onclick='browse(this.id)'><span class='glyphicon glyphicon-folder-open' style='text-decoration:none;color:#ffd11a;font-size:100px' aria-hidden='true'></span></a></p> <p id='fname' style='text-decoration:none;width:100px;font-size:22.5px;padding-left:2%'>"+data.folders[i]+"</p><p><button id='rem/"+data.folders[i]+"' onclick='remove(this.id)' class='btn btn-danger btn-md'><span class='glyphicon glyphicon-remove' style='text-decoration:none;color:white' aria-hidden='true'></span>&nbsp;Remove</button></p></div>";
					//alert(str);	
				}
				cover=cover+str+"</div></section>";
				$(".content-wrapper").html(cover);
				$(".content-wrapper").show();
				if(currentdir===home){
					$(".bck").hide();
					$(".new").css("margin-top","2%");
				}
				else{
					$(".bck").show();
					$(".new").css("margin-top","-2.125%");
				}
			});
		});
		$(document).on("click","#create",function(e){
			$(".content-wrapper").hide();
			$(".newfolder").show();
			$("#fexists").hide();
		});
		$(document).on("click","#done",function(e){
			$("#done").bind("click",eventhandler);
			var result=validate();
		});
		if(currentdir===home){
			$(".bck").hide();
			$(".new").css("margin-top","2%");
		}
		else{
			$(".bck").show();
			$(".new").css("margin-top","-2.125%");
		}
	});
	function validate(){
		var folder=$("#foldername").val();
		var txt={};
		var res;
		var eventhandler = function(e) {
			e.preventDefault();
		}
		txt["currentdir"]=currentdir;
		txt["folder"]=folder;
		$.post("folderexists",txt, function(data, status) {
			console.log("data:"+data.result)
			if(data.result==true){
				$("#done").unbind("click",eventhandler);
				$("#fexists").show();
			}
			else{
				$("#fexists").hide();
				create(folder);
			}
		});
	}
	function create(folder){
		var d={};
		d["currentdir"]=currentdir;
		d["folder"]=folder;
		$.post("createfolder",d, function(data, status) {
			if(data.result==true){
				$(".newfolder").hide();
				reload();
			}
		});
	}
	function remove(folder){
		$(".content-wrapper").hide();
		var f=[];
		f=folder.split("/");
		var r=confirm("Are you sure about deleting "+f[1]);
		if(r==true){
			var d={};
			d["currentdir"]=currentdir;
			d["folder"]=f[1];
			$.post("deletefolder",d, function(data, status) {
				if(data.result==true){
					reload();
				}
			});
		}
	}
	function reload(){
		var t={};
		t["currentdir"]=currentdir;
		//alert(JSON.stringify(text));
		$.post("browsefolders",t, function(data, status) {
			console.log(data);
			var cover="<section class='container'><div class='row'>";
			var str=""
			for(var i=0;i<data.folders.length;i++){
				str=str+"<div class='col-sm-4 col-md-2' style='padding-bottom: 15px; height: 300px'> <p><a href='#' id='"+data.folders[i]+"' onclick='browse(this.id)'><span class='glyphicon glyphicon-folder-open' style='text-decoration:none;color:#ffd11a;font-size:100px' aria-hidden='true'></span></a></p> <p id='fname' style='text-decoration:none;width:100px;font-size:22.5px;padding-left:2%'>"+data.folders[i]+"</p><p><button id='rem/"+data.folders[i]+"' onclick='remove(this.id)' class='btn btn-danger btn-md'><span class='glyphicon glyphicon-remove' style='text-decoration:none;color:white' aria-hidden='true'></span>&nbsp;Remove</button></p></div>";
				//alert(str);	
			}
			cover=cover+str+"</div></section>";
			$(".content-wrapper").html(cover);
			str="";
			cover="";
			$(".content-wrapper").show();
			if(currentdir===home){
				$(".bck").hide();
				$(".new").css("margin-top","2%");
			}
			else{
				$(".bck").show();
				$(".new").css("margin-top","-2.125%");
			}
		});
	}
	
	function browse(id){
		fname=id;
		$(".content-wrapper").hide();
		currentdir=currentdir+"/"+fname;
		//alert(currentdir);
		var text={};
		text["currentdir"]=currentdir;
		//alert(JSON.stringify(text));
		$.post("browsefolders",text, function(data, status) {
			console.log(data);
			var cover="<section class='container'><div class='row'>";
			var str=""
			for(var i=0;i<data.folders.length;i++){
				str=str+"<div class='col-sm-4 col-md-2' style='padding-bottom: 15px; height: 300px'> <p><a href='#'  id='"+data.folders[i]+"' onclick='browse(this.id)'><span class='glyphicon glyphicon-folder-open' style='text-decoration:none;color:#ffd11a;font-size:100px' aria-hidden='true'></span></a></p> <p id='fname' style='text-decoration:none;width:100px;font-size:22.5px;padding-left:2%'>"+data.folders[i]+"</p><p><button id='rem/"+data.folders[i]+"' onclick='remove(this.id)' class='btn btn-danger btn-md'><span class='glyphicon glyphicon-remove' style='text-decoration:none;color:white' aria-hidden='true'></span>&nbsp;Remove</button></p></div>";
				//alert(str);	
			}
			cover=cover+str+"</div></section>";
			$(".content-wrapper").html(cover);
			str="";
			cover="";
			$(".content-wrapper").show();
			if(currentdir===home){
				$(".bck").hide();
				$(".new").css("margin-top","2%");
			}
			else{
				$(".bck").show();
				$(".new").css("margin-top","-2.25%");
			}
		});
	}
</script>
<style>
body {
	background-repeat: repeat;
}
.content-wrapper{
	margin-top:5%;
}
.wrapper nav {
	margin-left: 10%;
	margin-top: 2.5%;
	padding-bottom: 2.5%;
}

nav li {
	list-style: none;
	display: inline;
}

.thumbnail {
	box-shadow: 0 1px 1px 0.5px;
}

.thumbnail .caption {
	background-color: #ccffff;
}
.newfolder{
	margin-top:10%;
	margin-left:30%;
}
.newfolder li{
	list-style:none;
	display:inline-block;
}
.newfolder li:last-child{
	padding-left:2%;
}
.content-wrapper .row{
	height:700px;
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
					style="display: inline-block; list-style: none; margin-left: 60%"><h4><a href="homepage" id="hm" class="glyphicon glyphicon-home" style="text-decoration:none;color: white"
							aria-hidden="true"></a>&nbsp;&nbsp;${ses.role}</h4></li>
			</ul>
		</header>
		<div class="bck" style="margin-left:15%;margin-top:2%;">
			<button class="btn btn-info btn-md" id="back"><span class="glyphicon glyphicon-arrow-left" style="text-decoration:none"
								aria-hidden="true"></span>&nbsp;Back</button>
												
		</div>
		<div class="new" style="position:absolute;margin-left:25%;margin-top:2%;padding-bottom:7.5%;">
			<button class="btn btn-success btn-md" id="create"><span class="glyphicon glyphicon-plus" style="text-decoration:none;color:white"
								aria-hidden="true"></span>&nbsp;Add New</button>									
		</div>
		<div class="content-wrapper">
			<section class="container">
				<div class="row">
					<c:forEach items="${folders}" var="folder">
						<div class="col-sm-4 col-md-2"
							style="padding-bottom: 15px; height: 300px">
							<p><a href="javascript:void(0)" id="${folder}" onclick="browse(this.id)"><span class="glyphicon glyphicon-folder-open" style="text-decoration:none;color:#ffd11a;font-size:100px"
								aria-hidden="true"></span>
								</a></p>
							<p id="fname" style="text-decoration:none;width:100px;font-size:22.5px;padding-left:2%">${folder}</p>
							<p><button id="rem/${folder}" onclick="remove(this.id)" class='btn btn-danger btn-md'><span class='glyphicon glyphicon-remove' style='text-decoration:none;color:white' aria-hidden='true'></span>&nbsp;Remove</button></p>
						</div>
					</c:forEach>
				</div>
			</section>
		</div>
		<div class="newfolder">
				<div id="fexists" style="color:red">
				<h2>The folder already exists</h2>
				</div>
				<ul>
					<li><input id="foldername" type="text" style="width:200px"></li>
					<li><button id="done" class="btn btn-primary btn-md">Create</button></li>
				</ul>
		</div>
	</div>
</body>
</html>