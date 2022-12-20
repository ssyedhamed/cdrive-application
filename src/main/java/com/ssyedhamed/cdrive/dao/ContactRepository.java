package com.ssyedhamed.cdrive.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssyedhamed.cdrive.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{
	@Query("select email from Contact c where c.email = :email")
	public String getContactByUsername(@Param("email") String email);
	
	@Query("from Contact as c where c.user.id= :userId")
	public Page<Contact> getContactsOfUser(@Param("userId") long userId, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("delete Contact c where c.id= :cid")
	public void deleteContactById(@Param("cid") long cid);
}
