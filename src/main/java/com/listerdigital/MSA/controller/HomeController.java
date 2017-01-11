package com.listerdigital.MSA.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

//import com.listerdigital.MSA.dao.AWSConnection;
import com.listerdigital.MSA.dao.FolderDaoTest;
import com.listerdigital.MSA.domain.User;

@SuppressWarnings("unused")
@Controller
@SessionAttributes("name")
public class HomeController {
@RequestMapping(value="/")
public String welcome(Model model) throws IOException {
	model.addAttribute("greeting", "Getting Started");
	User user = new User();
	//AWSConnection s3=new AWSConnection();
    model.addAttribute("user", user);
    model.addAttribute("name","MSA");
	return "home";
}
}
