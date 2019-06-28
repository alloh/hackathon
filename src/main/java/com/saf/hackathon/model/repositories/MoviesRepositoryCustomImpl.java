package com.saf.hackathon.model.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.saf.hackathon.model.Movies;
import com.saf.hackathon.pojo.MovieInfor;

public class MoviesRepositoryCustomImpl implements MoviesRepositoryCustom {
	private static final String watched = "watched";
	private static final String regdate = "regDate";
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Movies> findMovies(MovieInfor filter) throws Exception {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Movies> requestCriteria = builder.createQuery(Movies.class);

		final Root<Movies> requestRoot = requestCriteria.from(Movies.class);
		Predicate criteria = builder.conjunction();
		if (filter.getWatched() != null) {
			criteria = builder.and(criteria,
					builder.equal(requestRoot.get(watched), filter.getWatched() == true ? 1 : 0));
		}

		requestCriteria.select(requestRoot).where(criteria).orderBy(builder.desc(requestRoot.get(regdate)));
		return entityManager.createQuery(requestCriteria).getResultList();
	}

	@Override
	public Long countServiceMovies(MovieInfor filter) throws Exception {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> clienteCriteria = builder.createQuery(Long.class);
		final Root<Movies> serviceRequestRoot = clienteCriteria.from(Movies.class);
		Predicate criteria = builder.conjunction();
		if (filter.getWatched() != null) {

			criteria = builder.and(criteria,
					builder.equal(serviceRequestRoot.get(watched), filter.getWatched() == true ? 1 : 0));
		}
		clienteCriteria.select(builder.count(serviceRequestRoot)).where(criteria);
		return entityManager.createQuery(clienteCriteria).getSingleResult();
	}

}