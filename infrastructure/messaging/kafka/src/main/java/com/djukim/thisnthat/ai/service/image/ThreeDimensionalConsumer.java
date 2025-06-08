//package com.djukim.thisnthat.ai.service.image;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaThreeDimensionalImageEventConsumer {
//
//    public static final String IMAGE_3D_TOPIC = "three-dimension-created-topic";
//    public static final String IMAGE_3D_DLT_TOPIC = "three-dimension-created-topic-dead";
//    public static final String IMAGE_3D_TOPIC_GROUP_ID =
//            "three-dimension-image-test12-consumer-group";
//    public static final String IMAGE_3D_DLT_TOPIC_GROUP_ID =
//            "three-dimension-image-test12-consumer-group-dead";
//
//    private final int MAX_RETRY_DLT = 1;
//
//    @KafkaListener(
//            topics = IMAGE_3D_TOPIC,
//            groupId = IMAGE_3D_TOPIC_GROUP_ID,
//            containerFactory = "threeDimensionalImageKafkaListenerContainerFactory")
//    public void listenThreeDimensionalImageCreated(String payload, Acknowledgment ack) {
//        try {
//            ThreeDimensionalImage image =
//                    ObjectMapperUtil.deserialize(payload, ThreeDimensionalImage.class);
//            log.info(
//                    "Received 3D image from Kafka: fileName={}, size={}, fileId={}",
//                    image.getFileName(),
//                    image.getSize(),
//                    image.getFileId());
//            log.info("Before publish");
//
//            try {
//                DomainEvents.publish(image.toEvent());
//            } catch (Exception domainEx) {
//                log.error("DomainEvents publish error: {}", domainEx.getMessage(), domainEx);
//                throw domainEx;
//            }
//            log.info("After publish");
//            ack.acknowledge();
//            log.info("ack complete");
//        } catch (Exception e) {
//            log.error("Kafka Consumer Error", e);
//            throw e;
//            // throw new RuntimeException("3D 이미지 생성 중 오류 발생", e);
//        }
//    }
//
//    @KafkaListener(
//            topics = IMAGE_3D_DLT_TOPIC,
//            groupId = IMAGE_3D_DLT_TOPIC_GROUP_ID,
//            containerFactory = "threeDimensionalImageKafkaListenerContainerFactory")
//    public void listenDeadThreeDimensionalImageCreated(String payload, Acknowledgment ack) {
//
//        try {
//            ThreeDimensionalImage image =
//                    ObjectMapperUtil.deserialize(payload, ThreeDimensionalImage.class);
//            image.addRetryCount();
//
//            log.info(
//                    "DLT 메시지 수신: {}-{}, Retry Count: {}",
//                    IMAGE_3D_DLT_TOPIC,
//                    image.getFileName(),
//                    image.getRetryCount());
//
//            if (image.getRetryCount() > MAX_RETRY_DLT) {
//                log.info("RETRY 최대로 Fail EVENT 로 갑니다.");
//                DomainEvents.publish(image.toFailureEvent());
//            } else {
//                log.info("RETRY 최대로 DLT EVENT 로 갑니다.");
//                DomainEvents.publish(image.toDltEvent());
//            }
//            ack.acknowledge();
//        } catch (Exception e) {
//            log.error("DLT 메시지 처리 실패: {}", e.getMessage());
//            ack.acknowledge();
//        }
//
//    }
//}
