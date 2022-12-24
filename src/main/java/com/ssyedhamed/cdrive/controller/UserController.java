package com.ssyedhamed.cdrive.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssyedhamed.cdrive.dao.ContactRepository;
import com.ssyedhamed.cdrive.dao.UserRepository;
import com.ssyedhamed.cdrive.entities.Contact;
import com.ssyedhamed.cdrive.entities.User;
import com.ssyedhamed.cdrive.helper.FileUploadHelper;
import com.ssyedhamed.cdrive.helper.Message;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user") //every handler 100% dependent on this mapping, be cautious before removing
public class UserController {
	@Autowired
	private UserRepository userDao;
	@Autowired
	private ContactRepository contactDao;
	private User user=null;
	private String currentPage;
	private String contactId;
	
	@ModelAttribute
	public void sendCommonData(Model model, Principal principal) {
		String username = principal.getName();
		this.user = userDao.getUserByUserName(username);
		
		model.addAttribute("user", user);
	}
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("title", "My Dashboard");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add_contact")
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		String PROCESS_URL=	"post_contact";
		model.addAttribute("PROCESS_URL",PROCESS_URL);
		return "normal/add_contact";
	}
	
	
	@PostMapping("/post_contact")
	public String processContact(@Valid @ModelAttribute Contact contact,  BindingResult result,
			@RequestParam("contactImage") MultipartFile contactImage,
			Principal principal, Model model) {
		try {
			if(contact==null) {
				return "error";
			}
		
			if(result.hasErrors()) {
				model.addAttribute("contact",contact);
				return "normal/add_contact";
			}
	
//	
//			if(this.contactDao.getContactByUsername(contact.getEmail())!=null&&!contact.getEmail().equals("")) {
//				throw new Exception("Email already used. Make sure you have entered it properly");
//			}

			String userName = principal.getName();
			User user = this.userDao.getUserByUserName(userName);
			contact.setUser(user);
			
			if(contactImage.isEmpty()) {
				contact.setImage("default.png");
			}
			else if(!FileUploadHelper.isValidFile(contactImage)) {
				System.out.println("file not supported");
				System.out.println("FIle type :"+contactImage.getContentType());
				throw new Exception("File not supported!! "
						+ "Only png and jpg files allowed");
			}else {
				contact.setImage(contactImage.getOriginalFilename());				
				FileUploadHelper.uploadFile(contactImage);	
			}
			user.getContacts().add(contact);
			this.userDao.save(user);
			Message m=new Message("Contact added successfully", "success");
			String PROCESS_URL=	"post_contact";
			model.addAttribute("PROCESS_URL",PROCESS_URL);
			model.addAttribute("from","add");
			model.addAttribute("message",m);
			model.addAttribute("contact", new Contact());
		} catch (Exception e) {
			e.printStackTrace();
			Message m=new Message(e.getMessage(), "error");
			model.addAttribute("message",m);
			model.addAttribute("contact",contact);
			return "normal/add_contact";
		}
		
		return "normal/add_contact";
	}
	
	@GetMapping("/view_contacts/{page}")
	public String viewContacts(Model model, @PathVariable("page") Integer page) {
		Pageable pageable=PageRequest.of(page, 7);
		Page<Contact> contactsOfUser = contactDao.getContactsOfUser(this.user.getId(), pageable);

		model.addAttribute("title","All Contacts");
		if(contactsOfUser.getContent()!=null) {			
			model.addAttribute("contacts",contactsOfUser.getContent());
		}
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contactsOfUser.getTotalPages());
		
		return "normal/view_contacts";
	}
	@GetMapping("/{contactId}/show_contact/{currentPage}")
	public String showSingleContact(@PathVariable("contactId") String cid,@PathVariable("currentPage") String currentPage, Model m) {
		Contact contact = this.contactDao.findById(Long.parseLong(cid)).get();
		if(user.getId()!=contact.getUser().getId()) {
			System.out.println("you have no access to other user(s)' contacts");
			return "error";
		}
		m.addAttribute("contact", contact);
		m.addAttribute("from","view");
		m.addAttribute("title","Contact Information");
		m.addAttribute("currentPage",currentPage);
		return "normal/open_contact";
	}
	@GetMapping("/{contactId}/edit_contact/{currentPage}")
	public String editContactForm(@PathVariable("contactId") String contactId,
			@PathVariable("currentPage") String currentPage, Model m) {
		if(currentPage==null) {
			return "redirect:/login";
		}
		this.contactId=contactId;
		Optional<Contact> contactOptional = this.contactDao.findById(Long.parseLong(contactId));
		if(contactOptional.isEmpty()) {
			System.out.println("from edit contact form");
			return "redirect:/login";
		}
		Contact contact =contactOptional.get();
		if(user.getId()!=contact.getUser().getId()) {
			System.out.println("you have no access to other user(s)' contacts");
			return "error";
		}
		
		this.currentPage=currentPage;
		m.addAttribute("title","Edit Contact");
		m.addAttribute("currentPage",currentPage);
		m.addAttribute("contact",contact);
		m.addAttribute("from","edit");
		String PROCESS_URL=	"put_contact";
		m.addAttribute("PROCESS_URL",PROCESS_URL);
		return "normal/edit_contact";
	}
	@PostMapping("/put_contact")
	public String updateContact(@Valid @ModelAttribute Contact contact,
			BindingResult result ,@RequestParam("file")MultipartFile file,
			@RequestParam("currentPage") String currentPage,
			@RequestParam("cid") String cid,
			Model m,
			RedirectAttributes redirectAttr) throws IOException {
		if(result.hasErrors()) {
			m.addAttribute("title","Edit Contact");
			m.addAttribute("currentPage",this.currentPage);
			m.addAttribute("contact",contact);
			m.addAttribute("from","edit");
			String PROCESS_URL=	"put_contact";
			m.addAttribute("PROCESS_URL",PROCESS_URL);
			return "normal/edit_contact";
//			return "redirect:/user/"+contactId+"/edit_contact/"+currentPage;
		}
		
		
		if(file.isEmpty()&&!file.getOriginalFilename().isBlank()) {
	
			redirectAttr.addFlashAttribute("message", "Image is corrupted. Try selecting other image");
			
			return "redirect:/user/"+cid+"/show_contact/"+currentPage;
		}
			if(!file.getOriginalFilename().isBlank()) {
				
				///Image type validation
				if(!FileUploadHelper.isValidFile(file)) {
					
					redirectAttr.addFlashAttribute("message", "File type not supported. Only png/jpeg files are allowed");
					
					return "redirect:/user/"+cid+"/show_contact/"+currentPage;
				}
				
				
				
				
				Optional<Contact> contactOpt = this.contactDao.findById(Long.parseLong(cid));
				if(contactOpt.isPresent()) {
					Contact contactFromDao=contactOpt.get();
					if(this.user.getId()!=contactFromDao.getUser().getId()) {
						System.out.println("cannot access");
						return "error";
					}
					FileUploadHelper.deleteFile(contactFromDao.getImage());
					FileUploadHelper.uploadFile(file);
					contactFromDao.setImage(file.getOriginalFilename());
					this.contactDao.save(contactFromDao);
				}
				
				return "redirect:/user/"+cid+"/show_contact/"+currentPage;
			}else {
				
			
		
		
		if(this.currentPage==null) {
			return "redirect:/login";
		}
		System.out.println(contact +" <-- contact from path");
		Contact contactFromDao = this.contactDao.findById(Long.parseLong(this.contactId)).get();
		contact.setId(contactFromDao.getId());
		contact.setImage(contactFromDao.getImage());
		System.out.println(contact +" <-- contact from path");
		System.out.println(contactFromDao +" <-- contact from database");
//		System.out.println(this.user+ " <-- user from principal");
		
		
		contact.setUser(this.user);
		System.out.println(contact +" <-- contact after update");
//		System.out.println(this.user+ " <-- user from principal");
		this.contactDao.save(contact);
		
		
		return "redirect:view_contacts/"+this.currentPage;
			}
	}
	@GetMapping("/{contactId}/delete_contact/{currentPage}")
	public String deleteContact(@PathVariable String contactId, @PathVariable String currentPage,
			
			RedirectAttributes attr,
			Model model) {
		if(this.contactDao.findById(Long.parseLong(contactId)).isEmpty()) {
			return "redirect:/login";
		}
		System.out.println("contact to be deleted :"+this.contactDao.findById(Long.parseLong(contactId)).get());
		System.out.println("page to be returned :"+currentPage);
		Contact contact = this.contactDao.findById(Long.parseLong(contactId)).get();
		if(this.user.getId()!=contact.getUser().getId()) {
			return "redirect:/login";
		}
		//deleting contact from db
		this.contactDao.deleteContactById(Long.parseLong(contactId));
		//deleting image from path
		FileUploadHelper.deleteFile(contact.getImage());
		attr.addFlashAttribute("message","Contact deleted successfully");
		
		Message msg= new Message("Contact deleted successfully", "success");
		model.addAttribute("message", msg);
		model.addAttribute("currentPage",this.currentPage);
		model.addAttribute("contacts",new Contact());
		
		return "redirect:/user/view_contacts/"+currentPage;
	}
	@GetMapping("/remove-photo/{cid}/{currentPage}")
	public String removePhoto(@PathVariable("cid")String cid, @PathVariable("currentPage") String currentPage) {
		Contact contact = this.contactDao.findById(Long.parseLong(cid)).get();
		FileUploadHelper.deleteFile(contact.getImage());
		contact.setImage("default.png");
		this.contactDao.save(contact);
		return "redirect:/user/"+cid+"/show_contact/"+currentPage;
	}
	
}
