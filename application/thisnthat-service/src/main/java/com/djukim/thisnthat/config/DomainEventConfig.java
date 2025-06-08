package com.djukim.thisnthat.config;

import com.djukim.thisnthat.event.DomainEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DomainEventConfig {

    @Bean
    public InitializingBean initializingBean(ApplicationContext applicationContext) {
        return () -> DomainEvents.setPublisher(applicationContext);
    }
}
