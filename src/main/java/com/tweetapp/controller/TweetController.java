package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dto.ErrorResponse;
import com.tweetapp.dto.Reply;
import com.tweetapp.dto.TweetUpdate;
import com.tweetapp.entities.Tweet;
import com.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.services.TweetService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tarun Prajapat
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
public class TweetController {

//	Injected TweetService bean
	@Autowired
	private TweetService tweetService;

////  Kafka Configuration
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
//	
////	Kafka Topic Name
    private static final String KAFKA_TOPIC = "tweets";

	/**
	 * Controller Method to get all tweets HTTP GET Request
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/all
	 */
	@GetMapping(value = "/tweets/all")
	public ResponseEntity<?> getAllTweets(@RequestHeader(value = "loggedInUser") String loggedInUser) {
		try {
			return new ResponseEntity<>(tweetService.getAllTweets(loggedInUser), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to get all tweets of a user HTTP GET Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram
	 */
	@GetMapping(value = "/tweets/{username}")
	public ResponseEntity<?> getUserTweets(@PathVariable("username") String username,
			@RequestHeader(value = "loggedInUser") String loggedInUser) {
		try {
			return new ResponseEntity<>(tweetService.getUserTweets(username, loggedInUser), HttpStatus.OK);
		} catch (InvalidUsernameException e) {
			return new ResponseEntity<>(new ErrorResponse("Invalid User param received"),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to post a new tweet HTTP POST Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/add
	 */
	@PostMapping(value = "/tweets/{username}/add")
	public ResponseEntity<?> postNewTweet(@PathVariable("username") String username, @RequestBody Tweet newTweet) {
//		log.info("posting tweet message sent to: " + KAFKA_TOPIC);
		return new ResponseEntity<>(tweetService.postNewTweet(username, newTweet), HttpStatus.CREATED);
	}

	/**
	 * Controller method to get tweet and its details HTTP GET Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/ibe2226137b37328
	 */
	@GetMapping(value = "/tweets/{username}/{tweetId}")
	public ResponseEntity<?> getTweetDeatils(@PathVariable("username") String username,
			@PathVariable("tweetId") String tweetId) {
		try {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " is fetching a tweet and it's details.");
			return new ResponseEntity<>(tweetService.getTweet(tweetId, username), HttpStatus.OK);
		} catch (Exception e) {
		kafkaTemplate.send(KAFKA_TOPIC,
					username + " is fetching a tweet and its details but encountered server error.");
			return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to update an existing tweet HTTP PUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/update
	 */
	@PutMapping(value = "/tweets/{username}/update")
	public ResponseEntity<?> updateTweet(@PathVariable("username") String username,
			@RequestBody TweetUpdate tweetUpdate) {
		try {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " has updated a tweet.");
			return new ResponseEntity<>(
					tweetService.updateTweet(username, tweetUpdate.getTweetId(), tweetUpdate.getTweetText()),
					HttpStatus.OK);
		} catch (TweetNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encountered an error while updating a tweet.");
			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encountered server error while updating a tweet.");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to delete an existing tweet HTTP DELETE Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/delete
	 */
	@DeleteMapping(value = "/tweets/{username}/delete")
	public ResponseEntity<?> deleteTweet(@PathVariable("username") String username,
			@RequestHeader(value = "tweetId") String tweetId) {
		try {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has deleted a tweet.");
			return new ResponseEntity<>(tweetService.deleteTweet(tweetId), HttpStatus.OK);
		} catch (TweetNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while deleting a tweet");
			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a  server error while deleting a tweet");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to like an existing tweet HTTP PUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/like/ibe2226137b37328
	 */
	@PutMapping(value = "/tweets/{username}/like/{tweetId}")
	public ResponseEntity<?> likeATweet(@PathVariable("username") String username,
			@PathVariable(value = "tweetId") String tweetId) {
		try {
			kafkaTemplate.send(KAFKA_TOPIC, username + " liked a tweet.");
			return new ResponseEntity<>(tweetService.likeTweet(username, tweetId), HttpStatus.OK);
		} catch (TweetNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while liking a tweet");
			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while liking a tweet");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to Dislike an existing tweet HTTP GUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/dislike/ibe2226137b37328
	 */
	@PutMapping(value = "/tweets/{username}/dislike/{tweetId}")
	public ResponseEntity<?> dislikeATweet(@PathVariable("username") String username,
			@PathVariable(value = "tweetId") String tweetId) {
		try {
			kafkaTemplate.send(KAFKA_TOPIC, username + " disliked a tweet");
			return new ResponseEntity<>(tweetService.dislikeTweet(username, tweetId), HttpStatus.OK);
		} catch (TweetNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while disliking a tweet");
			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while deleting a tweet");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to comment on an existing tweet HTTP GUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/reply/ibe2226137b37328
	 */
	@PostMapping(value = "/tweets/{username}/reply/{tweetId}")
	public ResponseEntity<?> replyToTweet(@PathVariable("username") String username,
			@PathVariable("tweetId") String tweetId, @RequestBody Reply tweetReply) {
		try {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has commented on a tweet.");
			return new ResponseEntity<>(tweetService.replyTweet(username, tweetId, tweetReply.getComment()),
					HttpStatus.OK);
		} catch (TweetNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while commenting on a tweet");
			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while commenting on a tweet");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
