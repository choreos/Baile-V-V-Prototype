package br.usp.ime.ws.traveler;

public class Request {
	
	private String message;
	private String operation;
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}

}
