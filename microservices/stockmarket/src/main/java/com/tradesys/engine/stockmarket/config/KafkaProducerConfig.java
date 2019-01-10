package com.tradesys.engine.stockmarket.config;

import com.tradesys.engine.stockmarket.utils.KafkaErrorMsg;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
/**
 * Class under development
 */
public class KafkaProducerConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = defaultConfigPros();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    ProducerFactory<String, KafkaErrorMsg> kafkaErrorMsgProducerFactory() {
        Map<String, Object> configProps = defaultConfigPros();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, KafkaErrorMsg> logProducerFactory() {
        return new KafkaTemplate<>(kafkaErrorMsgProducerFactory());
    }

    private Map<String, Object> defaultConfigPros() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaConfig.getBootstrapAddress());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return configProps;
    }
}
