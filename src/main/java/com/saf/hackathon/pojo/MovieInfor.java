package com.saf.hackathon.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Holds movie information
 * 
 * @author alagat
 *
 */

public class MovieInfor {
	private String id;
	private String title;
	private String description;
	private String recommendation;
	@Min(value = 1)
	@Max(value = 5)
	private Integer rating;
	private Boolean watched;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Boolean getWatched() {
		return watched;
	}

	public void setWatched(Boolean watched) {
		this.watched = watched;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
