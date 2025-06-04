package com.naga.multi_database.config;


import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoDbConfig {

    @Primary
    @Bean(name = "mongodbProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.mongodb")
    public MongoProperties getDbProps(){
        return new MongoProperties();
    }

    @Bean(name = "mongoTemplateConfig")
    @Primary
    public MongoTemplate getMongoTemplate(){
        return new MongoTemplate(mongodbDatabaseFactory(getDbProps()));
    }

    @Bean(name = "mongodbDatabaseFactory")
    @Primary
    public MongoDatabaseFactory mongodbDatabaseFactory(MongoProperties dbProps) {
        return new SimpleMongoClientDatabaseFactory(dbProps.getUri());
    }


}
