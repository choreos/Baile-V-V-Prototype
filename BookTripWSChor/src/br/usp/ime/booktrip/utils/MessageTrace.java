package br.usp.ime.booktrip.utils;

public class MessageTrace {
	
	private String emissor;
	private String receptor;
	private String name;
	private String content;
	
	public MessageTrace(String emissor, String receptor, String name, String content){
		this.emissor = emissor;
		this.receptor = receptor;
		this.name = name;
		this.content = content;
	}
	
	public String getEmissor(){
		return emissor;
	}
	
	public String getReceptor(){
		return receptor;
	}
		
	public String getName() {
		return name;
	}
	
	public String getContent() {
		return content;
	}	

}
