package com.cognizant.moviebookingapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.cognizant.moviebookingapp.model.Movie;

@EnableMongoRepositories
public interface MovieRepository extends MongoRepository<Movie, String> {

	Optional<Movie> findByMovieId(String movieId);

	boolean existsByMovieId(String movieId);

	boolean existsByMovieName(String movieName);

	Optional<Movie> findByMovieName(String movieName);

}
