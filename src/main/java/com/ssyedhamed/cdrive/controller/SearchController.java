package com.ssyedhamed.cdrive.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssyedhamed.cdrive.dao.ContactRepository;
import com.ssyedhamed.cdrive.dao.UserRepository;
import com.ssyedhamed.cdrive.entities.Contact;
import com.ssyedhamed.cdrive.entities.User;

@RestController
public class SearchController {
	@Autowired
	UserRepository userDao;
	@Autowired
	ContactRepository contactDao;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<List<Contact>> searchContacts(@PathVariable("query") String query, Principal principal){
		User user = this.userDao.getUserByUserName(principal.getName());
		List<Contact> contacts = this.contactDao.findByNameContainingAndUser(query, user);
		return new ResponseEntity<>(contacts,HttpStatus.OK);
	}
}
