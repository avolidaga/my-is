package ru.se.info.tinder.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
//    @Value("${spring.kafka.consumer.bootstrap-servers}")
//    private String bootstrap;
//
//    @Bean
//    public ConsumerFactory<String, ResponseDto> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "response");
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.highload.notification.model.network.ResponseDto");
//        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.highload.order.model.network.ResponseDto");
//        return new DefaultKafkaConsumerFactory<>(configProps);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, ResponseDto> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, ResponseDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}
