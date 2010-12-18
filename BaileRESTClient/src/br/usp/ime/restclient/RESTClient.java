package br.usp.ime.restclient;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.Implementation;
import org.wiztools.restclient.HTTPMethod;
import org.wiztools.restclient.ReqEntityBean;
import org.wiztools.restclient.Request;
import org.wiztools.restclient.RequestBean;
import org.wiztools.restclient.RequestExecuter;
import org.wiztools.restclient.Response;
import org.wiztools.restclient.View;

public class RESTClient {

	private String baseURL = null;
	private Response response;
	
	
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getBaseURL() {
		return baseURL;
	}
	
	public void setResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	
	public String completeBaseURL(String suffix) throws EmptyBaseURLException {
		if(baseURL == null)
			throw new EmptyBaseURLException("Base url must be filled");

		return baseURL + suffix;
	}
	
	
	public String GET(String resource){
		Response response = execute(resource, HTTPMethod.GET, null );
		try { return response.getResponseBody().toString();	} 		
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	public String POST(String resource, String body){
		Response response = execute(resource, HTTPMethod.POST, body );
		return response.getHeaders().get("Location").iterator().next();		
	}
	
	
	public String PUT(String resource, String body){
		Response response = execute(resource, HTTPMethod.PUT, body );
		return response.getHeaders().get("Location").iterator().next();		
	}
	
	
	public Response execute(String target, HTTPMethod method, String body) {
		try {
			String url = completeBaseURL(target);

			RequestBean requestBean = new RequestBean();
			requestBean.setUrl(new URL(url));
			requestBean.setMethod(method);
			
			if(body != null){
				ReqEntityBean reqEntity = new ReqEntityBean(body.getBytes(Charsets.UTF_8), "text/plain", "UTF-8");
				requestBean.setBody(reqEntity);
			}
			
			Request request = requestBean;

			View view = new View(){
				  @Override
				  public void doStart(Request request){ /* do nothing*/   }
				  @Override
				  public void doCancelled(){ /* do nothing*/   }
				  @Override
				  public void doEnd(){ /* do nothing*/   }
				  @Override
				  public void doError(final String error) {System.err.println(error); }
				  @Override
				  public void doResponse(Response arg0) { setResponse(arg0); }
			};

		    RequestExecuter executer = Implementation.of(RequestExecuter.class);
			executer.execute(request, view);		
		} 
		
		catch (EmptyBaseURLException e) { e.printStackTrace(); } 
		catch (MalformedURLException e) { e.printStackTrace(); }
		
		return response;
	}
}
