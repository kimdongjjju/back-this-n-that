//package com.djukim.thisnthat.ai.service.image;
//
//@Override
//public void publishThreeDimensionalImageCreated(ThreeDimensionalImage threeDimensionalImage) {
//    try {
//        String json = objectMapper.writeValueAsString(threeDimensionalImage); // ✅ 직렬화
//        ProducerRecord<String, String> record =
//                new ProducerRecord<>(IMAGE_3D_TOPIC, threeDimensionalImage.getFileName(), json);
//
//        record.headers().add(
//                PUBLISH_RETRY_COUNT,
//                Integer.toString(threeDimensionalImage.getRetryCount())
//                        .getBytes(StandardCharsets.UTF_8));
//
//        CompletableFuture<SendResult<String, String>> future =
//                threeDimensionalImageKafkaTemplate.send(record);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                log.error("Failed to send 3D image to Kafka {}",
//                        threeDimensionalImage.getFileName(), ex);
//            } else {
//                log.info("3D image sent to Kafka: topic={}, offset={}, partition={}",
//                        result.getRecordMetadata().topic(),
//                        result.getRecordMetadata().offset(),
//                        result.getRecordMetadata().partition());
//            }
//        });
//    } catch (JsonProcessingException e) {
//        log.error("Failed to serialize 3D image", e);
//        throw new RuntimeException(e);
//    }
//}
//
