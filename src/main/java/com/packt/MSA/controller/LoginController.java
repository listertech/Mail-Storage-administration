package com.packt.MSA.controller;

import java.util.Map;

import javax.validation.Valid;

//import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.packt.MSA.domain.*;
 

@Controller
public class LoginController {
	
	
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String viewLogin(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        return "Home";
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
    	model.put("user", user);
        
        return "LoginSuccess";
    }
}
