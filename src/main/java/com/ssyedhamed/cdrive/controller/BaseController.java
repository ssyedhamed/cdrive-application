package com.ssyedhamed.cdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssyedhamed.cdrive.entities.User;

@Controller
public class BaseController {
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Home Page");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About CDrive");
		return "about";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("title","Registration");
		model.addAttribute("user",new User());
		return "register";
	}
	@GetMapping("/signin")
	public String login(Model model, @ModelAttribute("user") User user) {
		model.addAttribute("title","Login to CDrive");
		model.addAttribute("user",user);
		return "login";
	}

	
}
