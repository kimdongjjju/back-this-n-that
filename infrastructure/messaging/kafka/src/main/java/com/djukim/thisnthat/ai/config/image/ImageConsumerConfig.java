package com.djukim.thisnthat.ai.config.image;

import com.djukim.thisnthat.ai.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ImageConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, String> imageKafkaTemplate;
    private final int FETCH_MAX_BYTES_10MB = 10 * 1024 * 1024;
    private final int MAX_PARTITION_FETCH_BYTES_10MB = 10 * 1024 * 1024;


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "basic-consumer-group");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, FETCH_MAX_BYTES_10MB);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, MAX_PARTITION_FETCH_BYTES_10MB);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600000);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
        return new DeadLetterPublishingRecoverer(
                imageKafkaTemplate,
                (record, ex) -> {
                    log.error(
                            "DLT 전송: topic={}, key={}, 이유={}",
                            record.topic(),
                            record.key(),
                            ex.getMessage());
                    if (record.topic().endsWith("-dead")) {
                        log.warn("DLT 메시지는 재처리하지 않습니다: {}", record.key());
                        return null;
                    }
                    return new TopicPartition(record.topic() + "-dead", record.partition());
                });
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler() {
        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(deadLetterPublishingRecoverer(), new FixedBackOff(15000L, 2));
        errorHandler.setAckAfterHandle(false);
        errorHandler.setRetryListeners(
                (record, ex, deliveryAttempt) -> {
                    log.error(
                            "[DefaultErrorHandler] Kafka 처리 중 예외 (시도 {}): topic={}, key={},"
                                    + " offset={}, errorClass={}, errorMsg={}",
                            deliveryAttempt,
                            record.topic(),
                            record.key(),
                            record.offset(),
                            ex.getClass().getName(),
                            ex.getMessage(),
                            ex);
                });

        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> imageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(3);
        factory.getContainerProperties().setIdleBetweenPolls(120000); // 2분 동안 유휴 대기
        factory.getContainerProperties().setPollTimeout(120000); // poll()에서 최대 2분 대기

        factory.getContainerProperties().setLogContainerConfig(true);
        return factory;
    }
}
