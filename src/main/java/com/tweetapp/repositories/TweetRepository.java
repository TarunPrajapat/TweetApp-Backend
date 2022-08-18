package com.tweetapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.entities.Tweet;
import java.lang.String;
import java.util.List;

/**
 * @author Parichay Gupta
 * 
 * Persistance Layer for Tweet
 */
@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {
	/**
	 * 
	 * @return list of tweets done by user
	 * */
	List<Tweet> findByUsername(String username);

	/**
	 * @return list of tweets available with tweetID
	 * */
	List<Tweet> findByTweetId(String tweetId);
}
