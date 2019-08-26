package fi.csc.kafka.weather.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import fi.csc.kafka.weather.constants.KafkaConstants;

public class KafkaProducerFactory {

    public static KafkaProducer<Long,String> createKafkaProducer() {

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);

        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());

        return new KafkaProducer<Long, String>(props);

    }
}
