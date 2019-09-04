package fi.csc.kafka.weather.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import fi.csc.kafka.weather.commons.AppUtils;
import fi.csc.kafka.weather.constants.NetatmoConstants;

public class AuthTokenFactory {


	private String requestForTokens() throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(NetatmoConstants.NETATMO_AUTH_ENDPOINT);


		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "password"));
		params.add(new BasicNameValuePair("username", NetatmoConstants.NETATMO_USERNAME));
		params.add(new BasicNameValuePair("password", NetatmoConstants.NETATMO_PASSWORD));
		params.add(new BasicNameValuePair("client_id", NetatmoConstants.NETATMO_CLIENT_ID));
		params.add(new BasicNameValuePair("client_secret", NetatmoConstants.NETATMO_CLIENT_SECRET));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse response = client.execute(httpPost);

		System.out.println("Getting token from Password based grant");
		String tokens = EntityUtils.toString(response.getEntity());

		if(response.getStatusLine().getStatusCode()!=200)
			throw new RuntimeException(AppUtils.getErrorMessage(tokens));

		//System.out.println(tokens);


		client.close();

		return tokens;

	}

	public String getRefreshToken()
			throws ClientProtocolException, IOException, RuntimeException {

		String tokens = this.requestForTokens();
		if (tokens.isEmpty())
			throw new RuntimeException("Token string is empty");

		return AppUtils.parseTokenString(tokens, "refresh_token");
	}

	public String getToken()
			throws ClientProtocolException, IOException, RuntimeException {

		String tokens = this.requestForTokens();
		if (tokens.isEmpty())
			throw new RuntimeException("Token string is empty");

		return AppUtils.parseTokenString(tokens, "access_token");
	}


	public String getToken(String refresh_token)
			throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(NetatmoConstants.NETATMO_AUTH_ENDPOINT);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
		params.add(new BasicNameValuePair("refresh_token", refresh_token));
		params.add(new BasicNameValuePair("client_id", NetatmoConstants.NETATMO_CLIENT_ID));
		params.add(new BasicNameValuePair("client_secret", NetatmoConstants.NETATMO_CLIENT_SECRET));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse response = client.execute(httpPost);

		System.out.println("Getting token from Refresh Token based grant");
		String tokens = EntityUtils.toString(response.getEntity());

		if(response.getStatusLine().getStatusCode()!=200)
			throw new RuntimeException(AppUtils.getErrorMessage(tokens));

		//System.out.println(tokens);

		client.close();

		return AppUtils.parseTokenString(tokens, "access_token");
	}



}
