/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function box(){
                document.getElementById("login_form").style.boxShadow="0 12px 12px 6px";
                document.getElementById("login_form").style.zIndex="0";
                document.getElementById("wrap").style.zIndex="1";
            }
            function boxout(){
                document.getElementById("login_form").style.boxShadow="0 0px 0px 0px";
            }
            function foc(id){
            	if(id=="allocated"){
            		$("#allocated").removeAttr("type");
            		$("#allocated").removeAttr("type","number");
            	}
                document.getElementById(id).style.boxShadow="2px 8px 8px 2px";
                //document.getElementById(id).style.color="red";
            }
            function focout(id){
                document.getElementById(id).style.boxShadow="0px 1px 1px 0.5px";
                //document.getElementById(id).style.color="black";
            }
            function validate(){
               return false;
            }
            $(document).ready(function(){
                $('#login_form').mouseover(function(){
                   $('#login_form').css("background-color","whitesmoke");
                   $("#login_form").mouseover(function(){
                      box();
                   });
                   $("#login_form").mouseout(function(){
                      boxout();
                   });
                   /*
                   document.getElementById("ip1").onfocus=foc("ip1");
                   document.getElementById("ip1").onblur=focout("ip1");
                   document.getElementById("ip2").onfocus=foc("ip2");
                   document.getElementById("ip2").onblur=focout("ip2");
                   */
                });
            });