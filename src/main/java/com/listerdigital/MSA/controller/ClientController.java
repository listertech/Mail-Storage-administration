package com.listerdigital.MSA.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
//import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listerdigital.MSA.dao.*;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.service.Clientservice;
import com.listerdigital.MSA.service.Folderservice;

@SuppressWarnings("unused")
@Controller
@SessionAttributes("ses")
public class ClientController {
	Logger logger=LoggerFactory.getLogger(ClientController.class);
	
	String imagepath="";
	
	@RequestMapping(value = "/ClientDetails")
    public String clientDetails(@ModelAttribute(value ="ses") User user, Map<String, Object> model) {
        
    	
    	//System.out.println("Login Controller:"+user.getRole());
    	//model.put("user", user);
        //model.put("ses", user);
        Client client=new Client();
        model.put("client", client);
        return "ClientDetails";
    }
	
	@RequestMapping(value = "/CreateClient")
	public String createClient(@Valid @ModelAttribute(value="client") Client client,BindingResult result,Map<String, Object> model) throws IOException{
		if (result.hasErrors()){  	
            return "ClientDetails";
        }
		Folderservice fs=new Folderservice();
		fs.createFolder("MSA", client.getName());
		Folderservice updatefolder=new Folderservice();
		Clientservice cs=new Clientservice(client);
		model.put("client", client);
		return "ClientCreated";
	}
	
	@RequestMapping(value = "/verifyClient",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String verify(@RequestParam String name,Model model) throws IOException{
		Clientservice cs=new Clientservice();
		boolean result=cs.clientExists(name);
		model.addAttribute("result",result);
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		logger.info(jsonString);
		return jsonString;
	}
	/*
	@RequestMapping(value = "/uploadclientlogo",produces=MediaType.)
	public void updlogo(@Valid @ModelAttribute(value="client") Client client,BindingResult result,Map<String, Object> model) throws IOException{
		if (result.hasErrors()){  	
            return "ClientDetails";
        }
		FolderService fs=new FolderService();
		fs.createFolder("MSA", client.getName());
		FolderService updatefolder=new FolderService();
		ClientService cs=new ClientService(client);
		model.put("client", client);
		return "ClientCreated";
	}
	*/
	
	@RequestMapping(value = "/deleteClient")
	public String deleteclient(@RequestParam String cname,Model model) throws IOException{
		Clientservice cs=new Clientservice();
		System.out.println(cname);
		cs.deleteClient(cname);
		Folderservice fs=new Folderservice();
		fs.removeFolder("MSA", cname);
		fs=new Folderservice();
		return "redirect:/homepage";
	}
}
