package com.saf.hackathon.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the application_user database table.
 * 
 */
@Entity
@Table(name="APPLICATION_USER")
@NamedQuery(name="ApplicationUser.findAll", query="SELECT a FROM ApplicationUser a")
public class ApplicationUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String firstname;

	private String lastname;

	private String password;

	private String username;

	public ApplicationUser() {
	}



	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getLastname() {
		return lastname;
	}



	public void setLastname(String lastname) {
		this.lastname = lastname;
	}



	public String getFirstname() {
		return firstname;
	}



	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	
}