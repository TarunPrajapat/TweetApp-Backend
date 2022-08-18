package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.services.UserModelService;

/**
 * @author Parichay Gupta
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

//	injected bean UserModelService
	@Autowired
	private UserModelService userModelService;

	/**
	 * Controller method to retrive all the available users HTTP GET mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:/8082/api/v1.0/tweets/users/all
	 */
	@GetMapping(value = "/tweets/users/all")
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<>(userModelService.getAllUsers(), HttpStatus.OK);
	}

	// method to search for like users by username

	/**
	 * Controller method to find user by username HTTP GET mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:/8082/api/v1.0/tweets/user/search/ram@gmail.com
	 */
	@GetMapping(value = "/tweets/user/search/{username}")
	public ResponseEntity<?> searchForUsers(@PathVariable String username) {
		return new ResponseEntity<>(userModelService.getUsersByUsername(username), HttpStatus.OK);
	}
	
	 
	@GetMapping(value="tweets/user/profile/{username}")
	public ResponseEntity<?> searchUser(@PathVariable String username){
		return new ResponseEntity<>(userModelService.findByUsername(username), HttpStatus.OK);
	}

}
