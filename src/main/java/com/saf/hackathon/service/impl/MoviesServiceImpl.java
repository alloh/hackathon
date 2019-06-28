package com.saf.hackathon.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saf.hackathon.exception.ObjectNotFoundException;
import com.saf.hackathon.model.Movies;
import com.saf.hackathon.model.repositories.MoviesRepository;
import com.saf.hackathon.pojo.MovieInfor;
import com.saf.hackathon.service.MoviesService;
import com.saf.hackathon.service.UtilService;
@Service
public class MoviesServiceImpl implements MoviesService<Movies> {
	@Autowired
	private UtilService utilityService;
    @Value("${token.header}")
    private String headerKey;
    @Value("${movie.notfound}")
    private String movieNotFound;
    @Autowired
    private MoviesRepository moviesRepository;
	@Override
	public Movies prepareMoviesServiceForInsert(MovieInfor filter) throws Exception {
		Movies movies = new Movies();
		movies.setTitle(filter.getTitle());
		movies.setDescription(filter.getDescription());
		movies.setId(UUID.randomUUID().toString());
		movies.setLastUpdated(utilityService.getCurrentTimeStamp());
		movies.setRegDate(utilityService.getCurrentTimeStamp());
		movies.setRecommendation(filter.getRecommendation() != null ? filter.getRecommendation() : null);
		movies.setWatched(filter.getWatched() != null && filter.getWatched() != true ? 0 : 1);
		movies.setCreatedBy(utilityService.getCurrentRequest().getHeader(headerKey)!=null?utilityService.getCurrentRequest().getHeader(headerKey):"ADMIN" );
		movies.setUpdatedBy(utilityService.getCurrentRequest().getHeader(headerKey)!=null?utilityService.getCurrentRequest().getHeader(headerKey):"ADMIN" );
		movies.setRating(filter.getRating());
		return movies;
	}

	@Override
	public Movies prepareMovieServiceForUpdate(MovieInfor filter) throws Exception {
		Movies movie = moviesRepository.findById(filter.getId()).orElse(null);
		if(movie==null){
			throw new ObjectNotFoundException(movieNotFound);
		}
		movie.setTitle(filter.getTitle());
		movie.setRecommendation(filter.getRecommendation());
		movie.setWatched(filter.getWatched() != null && filter.getWatched() != true ? 0 : 1);
		movie.setLastUpdated(utilityService.getCurrentTimeStamp());
		return movie;
	}

}
