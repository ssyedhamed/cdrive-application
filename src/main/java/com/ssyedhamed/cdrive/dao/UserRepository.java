package com.ssyedhamed.cdrive.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssyedhamed.cdrive.entities.Contact;
import com.ssyedhamed.cdrive.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u from User u where u.email = :email")
		public User getUserByUserName(@Param("email") String email);
	
	
}
