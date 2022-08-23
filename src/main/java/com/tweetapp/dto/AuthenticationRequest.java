package com.tweetapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tarun Prajapat
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	private String username;

	private String password;

}
