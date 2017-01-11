package com.packt.MSA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.packt.MSA.domain.User;

@Controller
public class HomeController {
@RequestMapping("/")
public String welcome(Model model) {
	model.addAttribute("greeting", "Getting Started");
	User user = new User();
    model.addAttribute("user", user);
	return "home";
}
}
