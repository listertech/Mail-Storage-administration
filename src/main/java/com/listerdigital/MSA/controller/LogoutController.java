package com.listerdigital.MSA.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.listerdigital.MSA.dao.FolderDaoTest;
import com.listerdigital.MSA.domain.User;

@SuppressWarnings("unused")
@Controller
public class LogoutController {
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request){
		HttpSession httpSession = request.getSession();
        httpSession.invalidate();
		return "redirect:/";
	}
}
