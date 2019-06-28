package com.saf.hackathon.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.saf.hackathon.exception.PreconditionFailedException;
import com.saf.hackathon.facade.MoviesFacade;
import com.saf.hackathon.model.Movies;
import com.saf.hackathon.model.repositories.MoviesRepository;
import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.Modify;
import com.saf.hackathon.pojo.MovieInfor;
import com.saf.hackathon.pojo.MovieObj;
import com.saf.hackathon.service.MoviesService;
import com.saf.hackathon.util.annotation.Facade;
@Facade
public class MoviesFacadeImpl implements MoviesFacade {
	@Autowired
	private MoviesService<Movies> MoviesService;
	@Autowired
	private MoviesRepository MoviesRepository;
	@Value("${movie.exists}")
	private String movieExists;
	@Value("${movie.notfound}")
	private String movieNotFound;

	@Override
	public ResponseEntity<Add> addMovie(MovieInfor filter) throws Exception {
		Movies exist = MoviesRepository.findByTitle(filter.getTitle());
		if (exist != null) {
			throw new PreconditionFailedException(movieExists);
		}
		Movies Movies = MoviesService.prepareMoviesServiceForInsert(filter);
		MoviesRepository.save(Movies);
		Add add = new Add();
		add.setAdd(true);
		return ResponseEntity.ok(add);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public ResponseEntity<Modify> editMovie(MovieInfor filter) throws Exception {
		Movies Movies = MoviesService.prepareMovieServiceForUpdate(filter);
		MoviesRepository.save(Movies);
		Modify modify = new Modify();
		modify.setModify(true);
		return ResponseEntity.ok(modify);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public ResponseEntity<Boolean> removeMovie(String id) throws Exception {
		Movies movie = MoviesRepository.findById(id).orElse(null);
		if (movie == null) {
			throw new PreconditionFailedException(movieNotFound);
		}
		MoviesRepository.delete(movie);
		return ResponseEntity.ok(true);
	}

	@Override
	public ResponseEntity<List<MovieObj>> retriveAll() throws Exception {
        List<Movies> allMovies = MoviesRepository.findAll();
        List<MovieObj> allMovieData = new ArrayList<>();
        allMovies.stream().forEach(movie->{
        	MovieObj info = mapMoviesToMovieObj(movie);
        	allMovieData.add(info);
        });
		return ResponseEntity.ok(allMovieData);
	}

	@Override
	public ResponseEntity<MovieObj> retriveOne(String id) throws Exception {
		Movies movie = MoviesRepository.findById(id).orElse(null);
		if(movie!=null){
			MovieObj info = mapMoviesToMovieObj(movie);
			return ResponseEntity.ok(info);
		}
		return null;
	}

	private MovieObj mapMoviesToMovieObj(Movies movie) {
		MovieObj MovieObj = new MovieObj();
		MovieObj.setTitle(movie.getTitle());
		MovieObj.setDescription(movie.getDescription());
		MovieObj.setRecommendation(movie.getRecommendation());
		MovieObj.setWatched(movie.getWatched() == 0 ? false : true);
		return MovieObj;
	}

	@Override
	public ResponseEntity<List<MovieObj>> filteredResults(MovieInfor filter) throws Exception {
		List<Movies> filteredMovies = MoviesRepository.findMovies(filter);
		List<MovieObj> MovieObj = new ArrayList<>();
		filteredMovies.stream().filter(Objects::nonNull).forEach(movie->{
			MovieObj info = mapMoviesToMovieObj(movie);
			MovieObj.add(info);
		});
		return ResponseEntity.ok(MovieObj);
	}

}
