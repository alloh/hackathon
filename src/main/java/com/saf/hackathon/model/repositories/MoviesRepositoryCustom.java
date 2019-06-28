package com.saf.hackathon.model.repositories;

import java.util.List;

import com.saf.hackathon.model.Movies;
import com.saf.hackathon.pojo.MovieInfor;




public interface MoviesRepositoryCustom {
	Long countServiceMovies(MovieInfor filter) throws Exception;
	List<Movies> findMovies(MovieInfor filter) throws Exception;
}
