package fi.csc.kafka.weather.commons;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AppUtils {
	
	
	public static String parseTokenString(String json_str, String token_type) {
		
		if(!(token_type.equals("access_token") || token_type.equals("refresh_token")))
    		throw new RuntimeException("Unknown token type");
		
		String access_token = null;
		JsonParser parser = new JsonParser();
		JsonObject tokens_obj = (JsonObject) parser.parse(json_str);
		access_token = tokens_obj.get(token_type).getAsString();
		return access_token;
	}
	

	public static String getErrorMessage(String json_str) {
		
		String err_message = "";
		JsonParser parser = new JsonParser();
		JsonObject error_obj = (JsonObject) parser.parse(json_str);
		if(error_obj.get("error")!=null) {
			err_message = error_obj.get("error").getAsJsonObject().get("message").getAsString();
		}
		return err_message;
	}
	
	
}
