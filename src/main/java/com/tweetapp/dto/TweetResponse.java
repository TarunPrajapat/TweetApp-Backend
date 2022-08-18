package com.tweetapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class TweetResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String username;
	private String tweetText;
	private String firstName;
	private String lastName;
	private String tweetDate;
	private Integer likesCount;
	private Integer commentsCount;
	private Boolean likeStatus;
	private List<Comment> comments = new ArrayList<>();

}
