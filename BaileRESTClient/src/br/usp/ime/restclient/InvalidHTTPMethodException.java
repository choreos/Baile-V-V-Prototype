package br.usp.ime.restclient;

public class InvalidHTTPMethodException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidHTTPMethodException(String message){
		super(message);
	}

}
