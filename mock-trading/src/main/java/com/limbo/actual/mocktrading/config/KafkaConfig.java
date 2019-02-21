package com.limbo.actual.mocktrading.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory producerFactory(){
        ProducerFactory producerFactory = new DefaultKafkaProducerFactory(kafkaProperties.buildProducerProperties(),new StringSerializer(),new StringSerializer());
        return producerFactory;
    }

    @Bean
    public ConsumerFactory consumerFactory(){
        ConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties());
        ((DefaultKafkaConsumerFactory) consumerFactory).setKeyDeserializer(new StringDeserializer());
        ((DefaultKafkaConsumerFactory) consumerFactory).setValueDeserializer(new StringDeserializer());
        return consumerFactory;
    }

    @Bean
    public KafkaTemplate kafkaTemplate(){
        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory(), true);
        return kafkaTemplate;
    }

    @Bean
    public KafkaListenerContainerFactory<KafkaMessageListenerContainer> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
