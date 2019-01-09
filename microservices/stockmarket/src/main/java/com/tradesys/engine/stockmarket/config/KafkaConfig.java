package com.tradesys.engine.stockmarket.config;


import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
/**
 * Class under development
 */
public class KafkaConfig {

    private String bootstrapAddress;

    @Autowired
    //@TODO check if this is needed
    public KafkaConfig(Environment environment) {
        this.bootstrapAddress = environment.getProperty("spring.kafka.bootstrap-servers");
    }


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

//    @Bean
//    public NewTopic topic1() {
//        return new NewTopic("test", 1, (short) 1);
//    }

}
