package com.saf.hackathon.facade.impl;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.saf.hackathon.exception.DataIntegrityException;
import com.saf.hackathon.facade.UtilityFacade;


@com.saf.hackathon.util.annotation.Facade
public class UtilityFacadeImpl implements UtilityFacade {
	@Override
	public void validateRequest(BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (final FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getDefaultMessage() + "\n";
			}
			throw new DataIntegrityException(errorMessage);
		}

	}
}