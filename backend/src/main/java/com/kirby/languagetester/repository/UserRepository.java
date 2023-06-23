package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.todos.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUserNameAndPassword(String username,String password);
	
	User findByUserName(String username);
	

}
