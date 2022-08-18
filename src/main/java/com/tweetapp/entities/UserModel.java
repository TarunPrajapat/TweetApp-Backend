package com.tweetapp.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Parichay Gupta
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements Serializable {

	/**
	 * This will create collection in our MOngoDb with name Tweet and have the below
	 * fields
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private String contactNum;

}
