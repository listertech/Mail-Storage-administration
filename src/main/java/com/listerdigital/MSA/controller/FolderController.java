package com.listerdigital.MSA.controller;
import java.io.IOException;
import java.util.List;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listerdigital.MSA.dao.*;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.file.FolderReader;
import com.listerdigital.MSA.service.Folderservice;

@SuppressWarnings("unused")
@Controller
public class FolderController {
	@RequestMapping(value="/managefolders")
	public String manage(@RequestParam String fname,Map<String,Object> model){
		FolderReader fr=new FolderReader(); 
		List<String> folders=fr.getFolders("MSA/"+fname);
		model.put("folders", folders);
		model.put("currentdir", "MSA/"+fname);
		return "ManageFolders";
	}
	
	@RequestMapping(value="/browsefolders",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String browse(@RequestParam String currentdir,Map<String,Object> model) throws JsonProcessingException{
		FolderReader fr=new FolderReader(); 
		List<String> folders=fr.getFolders(currentdir);
		model.put("folders", folders);
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		return jsonString;
	}
	
	
	@RequestMapping(value="/folderexists",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String exists(@RequestParam String currentdir,@RequestParam String folder,Map<String,Object> model) throws JsonProcessingException{
		FolderReader fr=new FolderReader(); 
		List<String> folders=fr.getFolders(currentdir);
		int flag=0;
		for(int i=0;i<folders.size();i++){
			if(folder.equals(folders.get(i))){
				flag++;
			}
		}
		if(flag==0){
			model.put("result",false);
		}
		else{
			model.put("result", true);
		}
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		return jsonString;
	}
	
	@RequestMapping(value="/createfolder",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String create(@RequestParam String currentdir,@RequestParam String folder,Map<String,Object> model) throws IOException{
		Folderservice fs=new Folderservice();
		System.out.println(currentdir+"/"+folder);
		fs.createFolder(currentdir, folder);
		fs=new Folderservice();
		model.put("result", true);
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		return jsonString;
	}
	
	@RequestMapping(value="/deletefolder",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String delete(@RequestParam String currentdir,@RequestParam String folder,Map<String,Object> model) throws IOException{
		Folderservice fs=new Folderservice();
		System.out.println(currentdir+"/"+folder);
		fs.removeFolder(currentdir, folder);
		fs=new Folderservice();
		model.put("result", true);
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		return jsonString;
	}
}
