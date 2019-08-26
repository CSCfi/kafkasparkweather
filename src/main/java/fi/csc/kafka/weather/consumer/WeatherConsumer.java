package fi.csc.kafka.weather.consumer;

import java.time.Duration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import fi.csc.kafka.weather.constants.KafkaConstants;

public class WeatherConsumer {

	
	public void consume() {

		Consumer<Long, String> consumer = KafkaConsumerFactory.createConsumer();
		
		int noMessageFound = 0;
    
		while (true) {

			Duration polltime = Duration.ofSeconds(10);
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(polltime);
			// consumer will wait for 10 seconds, in case no record is found at the broker.
			if (consumerRecords.count() == 0) {
				noMessageFound++;

				if (noMessageFound > KafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
					// If no message found count is reached to threshold exit loop.  
					break;
				else
					continue;
			}
			//print each record. 

			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			});
			// commits the offset of record to broker. 
			consumer.commitAsync();
		}

		consumer.close();

	}


}
