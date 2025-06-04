package com.naga.multi_database.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.naga.multi_database.mongodb"},
mongoTemplateRef = MongoDbTemplateConfig.MONGO_TEMPLATE)
public class MongoDbTemplateConfig {

    protected static final String MONGO_TEMPLATE = "mongoTemplateConfig";
}
