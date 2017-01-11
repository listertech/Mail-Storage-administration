package com.listerdigital.MSA.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

//import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

//import com.listerdigital.MSA.dao.AWSConnection;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.file.ClientFile;
 

@Controller
@SessionAttributes({"ses","clientList"})
public class LoginController {
	
	
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String viewLogin(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        return "home";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(@Valid @ModelAttribute(value ="user") User user,
            BindingResult result, Map<String, Object> model) {
        
    	UserValidator validator=new UserValidator();
    	validator.validate(user, result);
    	
    	if (result.hasErrors()) {  
        	
            return "home";
        }
    	//System.out.println("Login Controller:"+user.getRole());
    	//model.put("user", user);
    	
        model.put("ses", user);
        ClientFile clientfile=new ClientFile();
        List<Client> clientList=clientfile.getallClients();
        model.put("clientList", clientList);
        //AWSConnection s3=new AWSConnection();
        return "LoginSuccess";
    }
    
    @RequestMapping(value = "/homepage")
    public String gohome(Map<String, Object> model) {
    	ClientFile clientfile=new ClientFile();
        List<Client> clientList=clientfile.getallClients();
        model.put("clientList", clientList);
    	return "LoginSuccess";
    }
}
