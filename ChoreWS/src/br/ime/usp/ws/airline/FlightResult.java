package br.ime.usp.ws.airline;

public class FlightResult {
	private String id;
	private String destination;
	private String date;
	private String time;
	
	
	public FlightResult(){}
	
	public FlightResult(String id, String destination, String date, String time){
		this.setId(id);
		this.setDestination(destination);
		this.setDate(date);
		this.setTime(time);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestination() {
		return destination;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}
}
