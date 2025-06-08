package com.djukim.thisnthat.ai.config.image;


import com.djukim.thisnthat.ai.properties.KafkaProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class CreateTopicConfig {

        private final KafkaProperties kafkaCustomProperties;

        private final int THREE_DIMENSIONAL_IMAGE_PARTITIONS = 3;
        private final short THREE_DIMENSIONAL_IMAGE_REPLICATION_FACTORY = 2;
        private final String THREE_DIMENSIONAL_MIN_IN_SYNC_REPLICAS = "2";

        private final int MAX_MESSAGE_BYTES_10MB = 10 * 1024 * 1024;

        public static final String IMAGE_3D_TOPIC = "image3d";
        public static final String IMAGE_3D_TOPIC_DLT = "image3d-dead";

        @PostConstruct
        public void createThreeDimensionalImageTopics() {
            String bootstrapServers = kafkaCustomProperties.getBootstrapServers();

            try (AdminClient adminClient =
                         AdminClient.create(
                                 Map.of(
                                         AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                                         bootstrapServers))) {
                for (String topicName : List.of(IMAGE_3D_TOPIC,IMAGE_3D_TOPIC_DLT)) {
                    if (adminClient.listTopics().names().get().contains(topicName)) {
                        log.info(
                                "Kafka topic '{}' already exists. Skip create, but apply config"
                                        + " update.",
                                topicName);
                        Map<ConfigResource, Collection<AlterConfigOp>> configUpdates =
                                getConfigResourceCollectionMap(topicName);
                        adminClient.incrementalAlterConfigs(configUpdates).all().get();
                        continue;
                    }

                    NewTopic newTopic =
                            new NewTopic(
                                    topicName,
                                    THREE_DIMENSIONAL_IMAGE_PARTITIONS,
                                    THREE_DIMENSIONAL_IMAGE_REPLICATION_FACTORY);
                    adminClient.createTopics(List.of(newTopic)).all().get();

                    log.info(
                            "Kafka topic '{}' created. partitions={}, replication={}",
                            topicName,
                            THREE_DIMENSIONAL_IMAGE_PARTITIONS,
                            THREE_DIMENSIONAL_IMAGE_REPLICATION_FACTORY);

                    Map<ConfigResource, Collection<AlterConfigOp>> configUpdates =
                            getConfigResourceCollectionMap(topicName);
                    adminClient.incrementalAlterConfigs(configUpdates).all().get();

                    log.info(
                            "Kafka topic '{}' updated with min.insync.replicas={}",
                            topicName,
                            THREE_DIMENSIONAL_MIN_IN_SYNC_REPLICAS);
                }

            } catch (ExecutionException | InterruptedException e) {
                log.error(" Kafka topic 생성 실패: {}", e.getMessage(), e);
            }
        }

        private Map<ConfigResource, Collection<AlterConfigOp>> getConfigResourceCollectionMap(
                String topicName) {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            return Map.of(
                    resource,
                    List.of(
                            new AlterConfigOp(
                                    new ConfigEntry(
                                            TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG,
                                            THREE_DIMENSIONAL_MIN_IN_SYNC_REPLICAS),
                                    AlterConfigOp.OpType.SET),
                            new AlterConfigOp(
                                    new ConfigEntry(
                                            TopicConfig.MAX_MESSAGE_BYTES_CONFIG,
                                            String.valueOf(MAX_MESSAGE_BYTES_10MB)),
                                    AlterConfigOp.OpType.SET)));
    }
}
