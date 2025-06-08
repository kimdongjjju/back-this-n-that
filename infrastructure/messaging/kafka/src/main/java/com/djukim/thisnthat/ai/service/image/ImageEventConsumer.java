package com.djukim.thisnthat.ai.service.image;

import com.djukim.thisnthat.event.DomainEvents;
import com.djukim.thisnthat.image.Image;
import com.djukim.thisnthat.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.djukim.thisnthat.ai.config.image.CreateTopicConfig.IMAGE_3D_TOPIC;
import static com.djukim.thisnthat.ai.config.image.CreateTopicConfig.IMAGE_3D_TOPIC_DLT;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageEventConsumer {

    @KafkaListener(topics = IMAGE_3D_TOPIC,
            groupId = "basic-consumer-group",
            containerFactory = "imageKafkaListenerContainerFactory")
    public void listen(@Payload String message, Acknowledgment ack) {
        try {
            Image image = ObjectMapperUtil.deserialize(message, Image.class);
            log.info("Received message: {}", message);
            DomainEvents.publish(image.toEvent());
            ack.acknowledge();
            log.info("acknowledged image complete: {}", image);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(
            topics = IMAGE_3D_TOPIC_DLT,
            groupId = "basic-consumer-group-dead",
            containerFactory = "imageKafkaListenerContainerFactory")
    public void deadListen(@Payload String payload, Acknowledgment ack) {

        try {
            Image image =
                    ObjectMapperUtil.deserialize(payload, Image.class);
            image.addRetryCount();

            log.info(
                    "DLT 메시지 수신: {}, Retry Count: {}",
                    IMAGE_3D_TOPIC_DLT,
                    image.getRetryCount());

            if (image.getRetryCount() > 2) {
                log.info("RETRY 최대로 Fail EVENT 로 갑니다.");
                //DomainEvents.publish(image.toFailureEvent());
            } else {
                log.info("RETRY 최대로 DLT EVENT 로 갑니다.");
                //DomainEvents.publish(image.toDltEvent());
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("DLT 메시지 처리 실패: {}", e.getMessage());
            ack.acknowledge();
        }

    }

}
