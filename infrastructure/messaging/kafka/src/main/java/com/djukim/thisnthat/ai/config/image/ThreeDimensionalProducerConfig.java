//package com.djukim.thisnthat.ai.config.image;
//
//@Configuration
//@RequiredArgsConstructor
//public class KafkaThreeDimensionalImageProducerConfig {
//
//    private final KafkaProperties kafkaProperties;
//
//    private final int MAX_REQUEST_SIZE_10MB = 10 * 1024 * 1024;
//    public final long BUFFER_MEMORY_32MB = 32 * 1024 * 1024;
//    public final int MAX_BLOCK_60S = 60000;
//
//    @Bean
//    public KafkaTemplate<String, String> threeDimensionalImageKafkaTemplate() {
//        return new KafkaTemplate<>(threeDimensionalImageProducerFactory());
//    }
//
//    public ProducerFactory<String, String> threeDimensionalImageProducerFactory() {
//        Map<String, Object> props = kafkaProperties.buildProducerProperties();
//        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        props.put(ACKS_CONFIG, "all");
//        props.put(RETRIES_CONFIG, Integer.toString(3));
//        props.put(MAX_REQUEST_SIZE_CONFIG, MAX_REQUEST_SIZE_10MB);
//        props.put(BUFFER_MEMORY_CONFIG, BUFFER_MEMORY_32MB);
//        props.put(MAX_BLOCK_MS_CONFIG, MAX_BLOCK_60S);
//        props.put(ENABLE_IDEMPOTENCE_CONFIG, true);
//        props.put(COMPRESSION_TYPE_CONFIG, "lz4");
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//}
//
//
//
