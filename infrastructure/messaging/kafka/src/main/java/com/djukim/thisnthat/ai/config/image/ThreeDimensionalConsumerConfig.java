//package com.djukim.thisnthat.ai.config.image;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class KafkaThreeDimensionalImageConsumerConfig {
//
//    private final KafkaProperties kafkaProperties;
//    private final int FETCH_MAX_BYTES_10MB = 10 * 1024 * 1024;
//    private final int MAX_PARTITION_FETCH_BYTES_10MB = 10 * 1024 * 1024;
//
//    private final KafkaTemplate<String, String> threeDimensionalImageKafkaTemplate;
//
//    @Bean
//    public Map<String, Object> threeDimensionalImageConsumerConfig() {
//        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
//
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//
//        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, FETCH_MAX_BYTES_10MB);
//        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, MAX_PARTITION_FETCH_BYTES_10MB);
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600000);
//        return props;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> threeDimensionalImageConsumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(this.threeDimensionalImageConsumerConfig());
//    }
//
//    @Bean
//    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
//        return new DeadLetterPublishingRecoverer(
//                threeDimensionalImageKafkaTemplate,
//                (record, ex) -> {
//                    log.error(
//                            "DLT 전송: topic={}, key={}, 이유={}",
//                            record.topic(),
//                            record.key(),
//                            ex.getMessage());
//                    if (record.topic().endsWith("-dead")) {
//                        log.warn("DLT 메시지는 재처리하지 않습니다: {}", record.key());
//
//                        return null;
//                    }
//
//                    return new TopicPartition(record.topic() + "-dead", record.partition());
//                });
//    }
//
//    @Bean
//    public DefaultErrorHandler defaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
//        DefaultErrorHandler errorHandler =
//                new DefaultErrorHandler(recoverer, new FixedBackOff(15000L, 2));
//
//        // 재시도 중 로그
//        errorHandler.setRetryListeners(
//                (record, ex, deliveryAttempt) -> {
//                    log.error(
//                            "[DefaultErrorHandler] Kafka 처리 중 예외 (시도 {}): topic={}, key={},"
//                                    + " offset={}, errorClass={}, errorMsg={}",
//                            deliveryAttempt,
//                            record.topic(),
//                            record.key(),
//                            record.offset(),
//                            ex.getClass().getName(),
//                            ex.getMessage(),
//                            ex);
//                });
//
//        return errorHandler;
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String>
//    threeDimensionalImageKafkaListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory,
//            DefaultErrorHandler defaultErrorHandler) {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory);
//        factory.setCommonErrorHandler(defaultErrorHandler);
//        factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
//        factory.setConcurrency(1);
//        factory.getContainerProperties().setIdleBetweenPolls(120_000); // 2분 동안 유휴 대기
//        factory.getContainerProperties().setPollTimeout(120_000); // poll()에서 최대 2분 대기
//        factory.getContainerProperties().setLogContainerConfig(true);
//        return factory;
//    }
//}