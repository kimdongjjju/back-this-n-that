package com.djukim.thisnthat.event;

import com.djukim.thisnthat.image.ImageConsumerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageEvent {

    @EventListener
    public void eventConsumerImage(ImageConsumerEvent event) throws InterruptedException {
        Thread.sleep(60000);
        log.info("비즈니스 컨슈머 로직 처리 : {}",event.toString());
    }
}
