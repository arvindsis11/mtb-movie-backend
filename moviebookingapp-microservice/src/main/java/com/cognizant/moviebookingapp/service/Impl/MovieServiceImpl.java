package com.cognizant.moviebookingapp.service.Impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.moviebookingapp.model.Movie;
import com.cognizant.moviebookingapp.repository.MovieRepository;
import com.cognizant.moviebookingapp.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {// --fix reponse entity

	@Autowired
	MovieRepository movieRepo;

	@Override
	public ResponseEntity<?> addMovie(Movie movie) {// it can be used for update because with same id it replaces
		movie.setTickets(new ArrayList<>()); // Initialize the list so it won't throw null exe. while booking
		movie.setBookedSeats(new HashSet<>());// Initialize the set so it won't throw null exe. while booking
		if (!movieRepo.existsByMovieName(movie.getMovieName())) {
			if (movie.getTotalTickets() > 0) {
				movie.setTicketStatus("BOOK ASAP");
				return new ResponseEntity<>(movieRepo.save(movie), HttpStatus.CREATED);
			}
			movie.setTicketStatus("SOLD OUT");
			return new ResponseEntity<>(movieRepo.save(movie), HttpStatus.CREATED);
		}
		return new ResponseEntity<>("movie already exists", HttpStatus.CONFLICT);
	}

	@Override
	public ResponseEntity<List<Movie>> getAllMovies() {

		return new ResponseEntity<>(movieRepo.findAll(), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> searchMovieById(String movieId) {
		Optional<Movie> movie = movieRepo.findByMovieId(movieId);
		if (movie.isPresent()) {
			return new ResponseEntity<>(movie.get(), HttpStatus.CREATED);
		}
		return new ResponseEntity<>("movie not found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> deleteMovie(String movieId) {
		Optional<Movie> movie = movieRepo.findByMovieId(movieId);
		if (movie.isPresent()) {
			movieRepo.deleteById(movie.get().getMovieId());
			return new ResponseEntity<>("movie deleted successfully!", HttpStatus.OK);
		}
		return new ResponseEntity<>("movie not found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> updateMovie(String movieName, int sumTickets) {

		if (sumTickets > 0) {
			Optional<Movie> movie = movieRepo.findByMovieName(movieName);
			if (movie.isPresent()) {
				int prevTotal = movie.get().getTotalTickets();
				int totalCountTickets = sumTickets + prevTotal;
				movie.get().setTotalTickets(totalCountTickets);// update total count of tickets
				movie.get().setTicketStatus("BOOK ASAP");
				movieRepo.save(movie.get());
				return new ResponseEntity<>("ticket status updated: " + totalCountTickets, HttpStatus.OK);
			}

			return new ResponseEntity<>("movie not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("ticket count should be greater then zero", HttpStatus.CONFLICT);
	}

	@Override
	public ResponseEntity<?> getBookedTicketList(String movieName) {
		// get tickets from moviedb that are already booked
		Optional<Movie> movie = movieRepo.findByMovieName(movieName);
		if (movie.isEmpty()) {
			return new ResponseEntity<>("movie not found", HttpStatus.NOT_FOUND);
		}
		Set<String> bookedSeats = movie.get().getBookedSeats();
		return new ResponseEntity<>(bookedSeats, HttpStatus.NOT_FOUND);
	}

}
