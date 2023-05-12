package com.cognizant.moviebookingapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.moviebookingapp.exception.AuthorizationException;
import com.cognizant.moviebookingapp.kafka.KafkaProducer;
import com.cognizant.moviebookingapp.model.Movie;
import com.cognizant.moviebookingapp.model.Ticket;
import com.cognizant.moviebookingapp.service.MovieService;
import com.cognizant.moviebookingapp.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class MovieController {

	@Autowired
	MovieService movieService;

	@Autowired
	TicketService ticketService;

	@Autowired
	AuthService authService;

	@Autowired
	private KafkaProducer stringProducer;

	// for testing purpose only
	@GetMapping("/test")
	@Operation(summary = "test endpoint to check if works")
	public String getName(@Parameter(hidden = true) @RequestHeader("Authorization") String token)
			throws AuthorizationException {
		System.out.println("token passed by user");
		System.err.println(token);// testing purpose only
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")) {
			System.out.println("it's working");
			stringProducer.sendMessage(token);
			return "admin only";
		} else {
			throw new AuthorizationException("Access Denied");
		}
	}

	/// role:admin
	@PostMapping("/addmovie")
	@Operation(summary = "add movie into db(admin)")
	public ResponseEntity<?> addMovies(@RequestBody Movie movie,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")) {
			return movieService.addMovie(movie);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getAllMovies")
	@Operation(summary = "get all available movies(admin+customer)")
	public ResponseEntity<List<Movie>> getAllMovies(
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")
				|| authService.validateToken(token).containsValue("ROLE_CUSTOMER")) {
			return movieService.getAllMovies();
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/movies/search/{movieId}")
	@Operation(summary = "searching movie by it(admin+customer)")
	public ResponseEntity<?> searchMovieById(@PathVariable(value = "movieId") String movieId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")
				|| authService.validateToken(token).containsValue("ROLE_CUSTOMER")) {
			return movieService.searchMovieById(movieId);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@PutMapping("/{movieName}/update/{sumTickets}")
	@Operation(summary = "for update of tickets into a movie(admin)")
	public ResponseEntity<?> updateTicketStatus(@PathVariable(value = "movieName") String movieName,
			@PathVariable(value = "sumTickets") int sumTickets,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")) {
			return movieService.updateMovie(movieName, sumTickets);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@DeleteMapping("/delete/{movieId}")
	@Operation(summary = "for deletion of movie(admin)")
	public ResponseEntity<?> deleteMovieById(String movieId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")) {
			// Allow access
			return movieService.deleteMovie(movieId);
		} else {
			// Deny access
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getAllTickets")
	@Operation(summary = "for listing all the tickets booked by users(admin)")
	public ResponseEntity<?> getAllTickets(@Parameter(hidden = true) @RequestHeader("Authorization") String token)
			throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")) {
			return ticketService.getAllTickets();
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@PostMapping("/book/{movieName}")
	@Operation(summary = "for book a ticket for a movie(admin+customer)")
	public ResponseEntity<?> bookMovie(@PathVariable(value = "movieName") String movieName, @RequestBody Ticket ticket,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {

			String userId = userInfo.keySet().iterator().next();
			ticket.setUserId(userId);
//			stringProducer.sendMessage("userId: " + userId + " movie: " + movieName);
			return ticketService.bookMovie(movieName, ticket);
		} else {
			// Deny access
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getUserTickets/{userId}") // no need of userId here--fix--if user is already logged in
	@Operation(summary = "for customer to see the booked tickets for a movie(admin+customer)")
	public ResponseEntity<?> getTicketsByUserId(@PathVariable(value = "userId") String userId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		// Map<String, String> userInfo = authService.validateToken(token);
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")
				|| authService.validateToken(token).containsValue("ROLE_CUSTOMER")) {
			// Allow access
			return ticketService.getTicketsUser(userId);
		} else {
			// Deny access
			throw new AuthorizationException("Access Denied");
		}

	}

	// get already booked seats list --planned(frontend)--testing purpose only
	@GetMapping("/get/bookedSeats/{movieName}")
	@Operation(summary = "for getting the booked seats for a movie(admin+customer)")
	public ResponseEntity<?> getBookedTicketList(@PathVariable(value = "movieName") String movieName,
			@RequestHeader("Authorization") String token) throws AuthorizationException {
		if (authService.validateToken(token).containsValue("ROLE_ADMIN")
				|| authService.validateToken(token).containsValue("ROLE_CUSTOMER")) {
			return movieService.getBookedTicketList(movieName);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

}
