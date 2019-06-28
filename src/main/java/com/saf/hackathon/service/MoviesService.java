package com.saf.hackathon.service;

import com.saf.hackathon.pojo.MovieInfor;

/**
 * 
 * @author user
 * Does all movie db related operations
 * @param <T>
 */
public interface MoviesService<T> {
  T prepareMoviesServiceForInsert(MovieInfor o) throws Exception;
  
  T prepareMovieServiceForUpdate(MovieInfor filter) throws Exception;
}
