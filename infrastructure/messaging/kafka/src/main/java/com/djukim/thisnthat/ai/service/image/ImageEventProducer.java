package com.djukim.thisnthat.ai.service.image;

import com.djukim.thisnthat.image.Image;
import com.djukim.thisnthat.image.ImagePublisher;
import com.djukim.thisnthat.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.djukim.thisnthat.ai.config.image.CreateTopicConfig.IMAGE_3D_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageEventProducer implements ImagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String PUBLISH_RETRY_COUNT = "2";

    @Override
    public void publish(Image image) {
            String payload = ObjectMapperUtil.serialize(image);
            ProducerRecord<String, String> record = new ProducerRecord<>(IMAGE_3D_TOPIC, payload);

            record.headers().add(
                    PUBLISH_RETRY_COUNT,
                    Integer.toString(image.getRetryCount()).getBytes(StandardCharsets.UTF_8)
            );

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send 3D image to Kafka {}", ex);
                } else {
                    log.info("3D image sent to Kafka: topic={}, offset={}, partition={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().offset(),
                            result.getRecordMetadata().partition());
                }
            });


    }
}

/*
auto.create.topics.enable=true
delete.topic.enable=true
default.replication.factor=3
min.insync.replicas=2
num.io.threads=4
num.network.threads=5
num.partitions=1
num.replica.fetchers=2
replica.lag.time.max.ms=30000
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
socket.send.buffer.bytes=102400
unclean.leader.election.enable=false
message.max.bytes=10485760
replica.fetch.max.bytes=10485760

 */