package com.djukim.thisnthat.event;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

public class DomainEvents {
    public static ApplicationEventPublisher publisher;

    public static void setPublisher(ApplicationEventPublisher publisher) {
        DomainEvents.publisher = publisher;
    }

    public static void publish(Object event) {
        if (Objects.isNull(publisher)) {
            return;
        }
        publisher.publishEvent(event);
    }
}
