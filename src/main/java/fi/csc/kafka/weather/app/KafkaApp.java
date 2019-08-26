package fi.csc.kafka.weather.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import fi.csc.kafka.weather.auth.AuthTokenFactory;
import fi.csc.kafka.weather.constants.NetatmoConstants;
import fi.csc.kafka.weather.producer.KafkaProducerFactory;
import fi.csc.kafka.weather.producer.WeatherProducer;

public class KafkaApp {

	public static void main(String[] args) {
		
		
		/* Uncomment the below section to obtain a refresh token for authenticating in future
		 * Using Password for every request is not recommended
		 * Store the refresh token in NetatmoConstants for future use. 
		 */
		
		/*
		AuthTokenFactory authfactory = new AuthTokenFactory();
		String refresh_token = authfactory.getRefreshToken();
		System.out.println(refresh_token);  // Prints the refresh token
		*/
		
		WeatherProducer producer = new WeatherProducer(
				KafkaProducerFactory.createKafkaProducer(),
				NetatmoConstants.NETATMO_REFRESH_TOKEN // Use the refresh token once you have it.
				);
		
		TimerTask task = new TimerTask() {
		      @Override
		      public void run() {
		    	  try {
		  			
		  			Map<String, String> hkiparams = new HashMap<String, String>();
		  			hkiparams.put("lon_ne", "24.94");
		  			hkiparams.put("lat_ne", "60.18");
		  			hkiparams.put("lon_sw", "24.93");
		  			hkiparams.put("lat_sw", "60.16");
		  			
		  			producer.produce("weatherHelsinki", hkiparams);
		  			
		  			
		  			Map<String, String> espparams = new HashMap<String, String>();
		  			espparams.put("lon_ne", "24.82");
		  			espparams.put("lat_ne", "60.18");
		  			espparams.put("lon_sw", "24.73");
		  			espparams.put("lat_sw", "60.16");
		  			
		  			//producer.produce("weatherEspoo", espparams);
		  			
		  			
		  			
		  		} catch (ClientProtocolException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		} catch (IOException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		}
		      }
		    };
		    
		    Timer timer = new Timer();
		    long delay = 0;
		    long intevalPeriod = 1 * 10;  // After every 5 seconds 
		    
		    // schedules the task to be run in an interval 
		    timer.scheduleAtFixedRate(task, delay,
		                                intevalPeriod);
		
		
		

	}

}
