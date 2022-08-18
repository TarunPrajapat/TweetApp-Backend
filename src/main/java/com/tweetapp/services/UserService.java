package com.tweetapp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.entities.UserModel;
import com.tweetapp.repositories.UserRepository;

/**
 * @author Parichay Gupta
 */
@Service
public class UserService implements UserDetailsService {

//	Injeted userRepository bean
	@Autowired
	private UserRepository userRepository;

	/**
	 * Used for validation
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel foundedUser = userRepository.findByUsername(username);
		if (foundedUser == null)
			return null;
		String name = foundedUser.getUsername();
		String password = foundedUser.getPassword();
		return new User(name, password, new ArrayList<>());
	}

}
