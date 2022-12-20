package com.ssyedhamed.cdrive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssyedhamed.cdrive.dao.UserRepository;
import com.ssyedhamed.cdrive.entities.User;
import com.ssyedhamed.cdrive.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class FormController {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	@PostMapping("/submit")
	public String submit(@Valid @ModelAttribute(value = "user") User user,BindingResult result, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement,
			Model model, HttpSession session ) {
	
		try {
			
			if(result.hasErrors()||!agreement) {
				model.addAttribute("user",user);
				return "register";
			}
			user.setRole("ROLE_USER");
			user.setImage("default.png");
			user.setActivated(true);
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			userRepo.save(user);
			model.addAttribute("user",new User());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message(e.getMessage(), "alert-danger") );
			return "register";
		}
		
		session.setAttribute("message",new Message("Successfully registered", "alert-success") );
		return "login";
	}
}
