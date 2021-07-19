package com.masterteknoloji.trafficanalyzer.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.District.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Camera.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Video.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Scenario.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Line.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Polygon.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.Direction.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.VideoRecord.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.CameraRecord.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.RawRecord.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails.class.getName(), jcacheConfiguration);
            cm.createCache(com.masterteknoloji.trafficanalyzer.domain.FileUploadTest.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
