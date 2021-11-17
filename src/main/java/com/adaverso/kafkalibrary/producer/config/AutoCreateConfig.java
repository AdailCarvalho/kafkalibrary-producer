package com.adaverso.kafkalibrary.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import com.adaverso.kafkalibrary.producer.enums.Topic;

/**
 * AutoCreate is not recommend for production environment. To make sure
 * autocreating won't create topics into a production Kafka Cluster, 
 * specify the profile using the @Profile annotation 
 *
 * */
@Configuration
@Profile("local")
public class AutoCreateConfig {

	@Bean
	public NewTopic libraryEvents() {
		return TopicBuilder.name(Topic.LIBRARY.getName())
						   .partitions(3)
						   .replicas(3)
						   .build();
	}
}
