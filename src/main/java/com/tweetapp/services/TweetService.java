package com.tweetapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.Comment;
import com.tweetapp.dto.TweetResponse;
import com.tweetapp.entities.Tweet;
import com.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.repositories.TweetRepository;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * @author Tarun Prajapat
 */
@Service
public class TweetService {

//	Injected TweetRepository bean
	@Autowired
	private TweetRepository tweetRepository;
	
	@Autowired
	KafkaTemplate<String, Tweet> kafkaTemplate;
	private static final String KAFKA_TOPIC = "tweets";
	
	Logger logger = LoggerFactory.getLogger(TweetService.class);
	/**
	 * Find all the available tweets
	 * 
	 * @return TweetResponse
	 */
	public List<TweetResponse> getAllTweets(String loggedInUser) {
		List<Tweet> allTweets = tweetRepository.findAll();
		List<TweetResponse> tweetResponse = allTweets.stream().map(tweet -> {
			Integer likesCount = tweet.getLikes().size();
			Boolean likeStatus = tweet.getLikes().contains(loggedInUser);
			Integer commentsCount = tweet.getComments().size();
			logger.info("All tweets --> {}",tweet);
			return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
					tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount,
					likeStatus, tweet.getComments());
		}).collect(Collectors.toList());
		return tweetResponse;
	}

	/**
	 * Method for searching tweets by a particular user
	 * 
	 * @return TweetResponse
	 * @throws InvalidUsernameException
	 */
	public List<TweetResponse> getUserTweets(String username, String loggedInUser) throws InvalidUsernameException {
		// use username as login id
		if (!StringUtils.isBlank(username)) {
			List<Tweet> tweets = tweetRepository.findByUsername(username);
			// List<TweetResponse> tweetResponse= new ArrayList<>();
			List<TweetResponse> tweetResponse = tweets.stream().map(tweet -> {
				Integer likesCount = tweet.getLikes().size();
				Boolean likeStatus = tweet.getLikes().contains(loggedInUser);
				Integer commentsCount = tweet.getComments().size();
				logger.info( username+" tweets --> {}",tweet);
				return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(), tweet.getFirstName(),
						tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount, likeStatus,
						tweet.getComments());
			}).collect(Collectors.toList());
			return tweetResponse;
		} else {
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}

	/**
	 * Method to post a new Tweet
	 * 
	 * @return Tweet
	 */
	public Tweet postNewTweet(String username, Tweet newTweet) {
		newTweet.setTweetId(UUID.randomUUID().toString());
		kafkaTemplate.send(KAFKA_TOPIC, newTweet);
		logger.info("The new tweet --> {}",newTweet);
		return tweetRepository.insert(newTweet);
	}

	/**
	 * Method to get tweet with tweetid
	 * 
	 * @return TweetResponse
	 * @throws TweetNotFoundException
	 */
	public TweetResponse getTweet(String tweetId, String username) throws TweetNotFoundException {
		Optional<Tweet> tweetFounded = tweetRepository.findById(tweetId);
		if (tweetFounded.isPresent()) {
			Tweet tweet = tweetFounded.get();
			Integer likesCount = tweet.getLikes().size();
			Boolean likeStatus = tweet.getLikes().contains(username);
			Integer commentsCount = tweet.getComments().size();
			logger.info("returned tweet --> {}",tweet);
			return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
					tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount,
					likeStatus, tweet.getComments());
		} else {
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}

	}

	/**
	 * Method to update an existing tweet
	 * 
	 * @return Tweet
	 * @throws TweetNotFoundException
	 */
	public Tweet updateTweet(String userId, String tweetId, String updatedTweetText) throws TweetNotFoundException {

		Optional<Tweet> originalTweetOptional = tweetRepository.findById(tweetId);
		if (originalTweetOptional.isPresent()) {
			Tweet tweet = originalTweetOptional.get();
			tweet.setTweetText(updatedTweetText);
			logger.info("Updated tweet --> {}",tweet);
			return tweetRepository.save(tweet);
		} else {
			logger.error("cannot update tweet since this tweet does not exist anymore.");
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}

	}

	/**
	 * Method to delete an existing tweet
	 * 
	 * @return boolean
	 * @throws TweetNotFoundException
	 */
	public boolean deleteTweet(String tweetId) throws TweetNotFoundException {
		if (tweetRepository.existsById(tweetId) && !StringUtils.isBlank(tweetId)) {
			tweetRepository.deleteById(tweetId);
			return true;
		} else {
			logger.error("Cannot delete tweet since this tweet does not exist anymore.");
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}
	}

	/**
	 * Method to like an existing tweet
	 * 
	 * @retun tweet
	 * @throws TweetNotFoundException
	 */
	public Tweet likeTweet(String username, String tweetId) throws TweetNotFoundException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.getLikes().add(username);
			logger.debug("Liked tweet --> {}",tweet);
			return tweetRepository.save(tweet);
		} else {
			logger.error("cannot like tweet since this tweet does not exist anymore.");
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}
	}

	/**
	 * Method to DisLike an existing tweet
	 * 
	 * @retun tweet
	 * @throws TweetNotFoundException
	 */
	public Tweet dislikeTweet(String username, String tweetId) throws TweetNotFoundException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.getLikes().remove(username);
			logger.info("Disliked tweets --> {}",tweet);
			return tweetRepository.save(tweet);
		} else {
			logger.error("cannot dislike tweet since this tweet does not exist anymore.");
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}
	}

	/**
	 * Method to comment on a tweet
	 * 
	 * @return tweet
	 * @throws TweetNotFoundException
	 */
	public Tweet replyTweet(String username, String tweetId, String tweetReply) throws TweetNotFoundException {
		Optional<Tweet> tweetFounded = tweetRepository.findById(tweetId);
		if (tweetFounded.isPresent()) {
			Tweet tweet = tweetFounded.get();
			Comment comment = new Comment(username, tweetReply);
			List<Comment> addList = new ArrayList<Comment>();
			addList.add(comment);
			tweet.getComments().add(comment);
			kafkaTemplate.send(KAFKA_TOPIC, tweet); // KAFKA SENDING EVENTS TO TOPIC
			logger.info("Commented on tweet --> {}",tweet);
			return tweetRepository.save(tweet);
		} else {
			logger.error("cannot comment on tweet since this tweet does not exist anymore.");
			throw new TweetNotFoundException("This tweet does not exist anymore.");
		}
	}

}
