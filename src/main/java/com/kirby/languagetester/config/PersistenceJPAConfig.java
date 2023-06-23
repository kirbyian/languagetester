package com.kirby.languagetester.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableCaching
@EnableJpaRepositories(basePackages="com.kirby.languagetester.repository")
public class PersistenceJPAConfig {
	
	@Bean
    public CacheManager cacheManager() {
        // Configure and return a cache manager implementation (e.g., ConcurrentMapCacheManager)
        return new ConcurrentMapCacheManager("tenses");
    }

}
