package fi.csc.kafka.weather.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import fi.csc.kafka.weather.auth.AuthTokenFactory;
import fi.csc.kafka.weather.commons.AppUtils;
import fi.csc.kafka.weather.commons.NetatmoAuthenticationException;
import fi.csc.kafka.weather.constants.NetatmoConstants;

public class NetatmoClient {

	boolean isAuthorized = false;
	private String refresh_token = "";
	private String access_token = "";
	
	
	public NetatmoClient() {
		// empty
	}
	public NetatmoClient(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	public String authorizeAndSetToken() throws ClientProtocolException, IOException {
		AuthTokenFactory token_factory = new AuthTokenFactory();
		String token = null;
		
		try {
			if(!isAuthorized && refresh_token.isEmpty())
				token = token_factory.getToken();
			
			if(!isAuthorized && !refresh_token.isEmpty())
				token = token_factory.getToken(this.getRefresh_token());
		
			System.out.println("the access token is");
			System.out.println(token);
			this.setAuthorized(true);
			this.setAccess_token(token);
			
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			setAuthorized(false);
			throw new RuntimeException(e);
		}
		
		
		return token;
	}
	
	
	public String getData(Map<String, String> params) throws IOException {
		
		if(!isAuthorized)
			throw new NetatmoAuthenticationException("Authorize first!");
		
		if(access_token.isEmpty())
			throw new NetatmoAuthenticationException("Access token missing!");
			
		
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(NetatmoConstants.NETATMO_PUBLIC_WEATHER_ENDPOINT);
	 
	    List<NameValuePair> postparams = new ArrayList<NameValuePair>();
	    postparams.add(new BasicNameValuePair("access_token", this.getAccess_token()));
	    
	    params.forEach((k,v)->postparams.add(new BasicNameValuePair(k,v)));
	    
	    httpPost.setEntity(new UrlEncodedFormEntity(postparams));
	    
	    
	    CloseableHttpResponse response = client.execute(httpPost);
	    
	    
	    
	    String data = EntityUtils.toString(response.getEntity());
	    
	    if(response.getStatusLine().getStatusCode()==403)
	    	throw new NetatmoAuthenticationException(AppUtils.getErrorMessage(data));
	    else if(response.getStatusLine().getStatusCode()!=200)
	    	throw new RuntimeException(AppUtils.getErrorMessage(data));
	    
	    //System.out.println(data);
	    
	    client.close();
		
	    return data;
		
	}
	
	
	
	public String getRefresh_token() {
		return refresh_token;
	}


	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}


	public boolean isAuthorized() {
		return isAuthorized;
	}


	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
