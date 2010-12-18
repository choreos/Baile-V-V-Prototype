package br.usp.ime.ws.traveler;

public class Flight {
	private String id;
	private String price;
	private String airline;
	private String status;
	
	
	public Flight(){}
	
	public Flight(String id, String price, String airline){
		this.setId(id);
		this.setPrice(price);
		this.setAirline(airline);
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getAirline() {
		return airline;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	
}
