package com.kirby.languagetester.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableCaching
@EnableJpaRepositories(basePackages="com.kirby.languagetester.repository")
public class PersistenceJPAConfig {
	

}
