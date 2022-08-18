package com.tweetapp.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.tweetapp.dto.Comment;

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
public class Tweet implements Serializable {

	/**
	 * This will create collection in our MOngoDb with name Tweet and have the
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String tweetId;

	private String username;

	private String tweetText;

	private String firstName;

	private String lastName;

	private String tweetDate;

	private List<String> likes = new ArrayList<>();

	private List<Comment> comments = new ArrayList<>();
}
