package com.tradesys.storageengine.config;

import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@AllArgsConstructor
public class MongoConfig extends AbstractMongoConfiguration {


    Environment environment;

    public String mongoDBHost() {
        return environment.getProperty("spring.data.mongodb.host");
    }

    public String mongoDBPort() {
        return environment.getProperty("spring.data.mongodb.port");
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(mongoDBHost(), Integer.valueOf(mongoDBPort()));
    }

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("spring.data.mongodb.database");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
