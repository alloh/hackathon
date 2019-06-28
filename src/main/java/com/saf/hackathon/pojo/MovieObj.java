package com.saf.hackathon.pojo;

public class MovieObj {
	private String title;
	private Integer rating;
	private Boolean watched;
	private String recommendation;
	private String description;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}