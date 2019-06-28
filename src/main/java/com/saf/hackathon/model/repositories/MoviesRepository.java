package com.saf.hackathon.model.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.saf.hackathon.model.Movies;

public interface MoviesRepository extends CrudRepository<Movies, String>, MoviesRepositoryCustom {
	Movies findByTitle(String title);

	List<Movies> findAll();
}
