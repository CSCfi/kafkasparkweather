
package fi.csc.kafka.weather.producer;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import fi.csc.kafka.weather.client.NetatmoClient;
import fi.csc.kafka.weather.commons.NetatmoAuthenticationException;

public class WeatherProducer {
	
	private KafkaProducer<Long, String> kp;
	
	private NetatmoClient client;
	
	

	public WeatherProducer(KafkaProducer<Long, String> kp) {
		this.kp = kp;
		NetatmoClient client = new NetatmoClient();
		this.client = client;
		
	}
	public WeatherProducer(KafkaProducer<Long, String> kp, String refresh_token) {
		this.kp = kp;
		NetatmoClient client = new NetatmoClient(refresh_token);
		this.client = client;
		
	}
	
	


	public void produce(String kafkaTopic, Map<String, String> params) throws IOException {
		
		String data="";
		
		for(int i=0; i<5; i++) { // 5 retries
			try {
				data = client.getData(params);
				break;
			} catch (NetatmoAuthenticationException e) {
				e.printStackTrace();
				System.out.println("Not authenticated! Authencating now....");
				client.authorizeAndSetToken();
			}
			
		}
		
		long time_key = Instant.now().getEpochSecond();
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(kafkaTopic, time_key, data);
		kp.send(record);
		
		
		System.out.println("Sent data to Kafka topic "+ kafkaTopic + " with key: " + time_key);
		
	}
	
	
}
