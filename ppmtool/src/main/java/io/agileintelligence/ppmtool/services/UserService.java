package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectException;
import io.agileintelligence.ppmtool.repositories.UserRepository;

@Service
public class UserService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			newUser.setUsername(newUser.getUsername());
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception ex) {
			throw new ProjectException("Username '" + newUser.getUsername() + "' already exists.");
		}
		// username needs to be unique
		
		// make sure that password and confirmPassword match
		
		// we don't show the password
	}
}
