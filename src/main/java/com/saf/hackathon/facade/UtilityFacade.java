package com.saf.hackathon.facade;


import org.springframework.validation.BindingResult;

public interface UtilityFacade {
	void validateRequest(BindingResult bindingResult) throws Exception;
	
}
