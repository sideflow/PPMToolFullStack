package io.agileintelligence.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6588475496629935097L;

	public ProjectException(String message) {
		super(message);
	}
	

}
