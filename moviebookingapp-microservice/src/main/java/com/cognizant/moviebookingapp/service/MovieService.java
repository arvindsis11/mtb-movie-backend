package com.cognizant.moviebookingapp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.moviebookingapp.model.Movie;

public interface MovieService {

	public ResponseEntity<?> addMovie(Movie movie);

	public ResponseEntity<List<Movie>> getAllMovies();
	
	public ResponseEntity<?> searchMovieById(String movieId);
	
	public ResponseEntity<?> deleteMovie(String movieId);
	
	public ResponseEntity<?> updateMovie(String movieName,int sumTickets);
	
	public ResponseEntity<?> getBookedTicketList(String movieName);

}
