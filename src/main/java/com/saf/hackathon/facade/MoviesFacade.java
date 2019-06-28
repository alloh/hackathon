package com.saf.hackathon.facade;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.Modify;
import com.saf.hackathon.pojo.MovieInfor;
import com.saf.hackathon.pojo.MovieObj;

/**
 * Handles movie related activities
 * @author alagat
 *
 */
public interface MoviesFacade {
	ResponseEntity<Add> addMovie(MovieInfor filter) throws Exception;
	
	ResponseEntity<Modify> editMovie(MovieInfor filter) throws Exception;

	ResponseEntity<Boolean> removeMovie(String id) throws Exception;
	
	ResponseEntity<List<MovieObj>> retriveAll() throws Exception;
	
	ResponseEntity<MovieObj> retriveOne(String id) throws Exception;

	ResponseEntity<List<MovieObj>> filteredResults(MovieInfor filter) throws Exception;
	
}
