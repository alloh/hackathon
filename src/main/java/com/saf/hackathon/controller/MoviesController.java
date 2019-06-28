package com.saf.hackathon.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saf.hackathon.facade.MoviesFacade;
import com.saf.hackathon.facade.UtilityFacade;
import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.Modify;
import com.saf.hackathon.pojo.MovieInfor;
import com.saf.hackathon.pojo.MovieObj;

@RestController
@RequestMapping("/saf/movies")
public class MoviesController {
	@Autowired
	private UtilityFacade utilityFacade;

	@Autowired
	private MoviesFacade moviesFacade;

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public ResponseEntity<MovieInfor> filter() throws Exception {

		return new ResponseEntity<>(new MovieInfor(), HttpStatus.OK);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Add> registerUser(@Valid @RequestBody MovieInfor filter, BindingResult bindingResult)
			throws Exception {
		utilityFacade.validateRequest(bindingResult);
		return moviesFacade.addMovie(filter);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity<Modify> editMovie(@Valid @RequestBody MovieInfor filter, BindingResult bindingResult)
			throws Exception {
		utilityFacade.validateRequest(bindingResult);
		return moviesFacade.editMovie(filter);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> removeMovie(@PathVariable(name = "id") String id) throws Exception {

		return moviesFacade.removeMovie(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MovieObj>> fetchAllMovies() throws Exception {

		return moviesFacade.retriveAll();
	}
	
	@RequestMapping(value = "/sortMovies", method = RequestMethod.POST)
	public ResponseEntity<List<MovieObj>> fetchFiltered(@Valid @RequestBody MovieInfor filter, BindingResult bindingResult)
			throws Exception {
		utilityFacade.validateRequest(bindingResult);
		return moviesFacade.filteredResults(filter);
	}
}

