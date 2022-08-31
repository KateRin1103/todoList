package com.example.todo.containers;

import com.example.todo.entity.User;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaContainerTest {

    private static final DockerImageName KAFKA_TEST_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest");

    private static final DockerImageName ZOOKEEPER_TEST_IMAGE = DockerImageName.parse(
            "confluentinc/cp-zookeeper:latest"
    );

    @Test
    public void testUsage() throws Exception {
        try (KafkaContainer kafka = new KafkaContainer(KAFKA_TEST_IMAGE)) {
            kafka.start();
            testKafkaFunctionality(kafka.getBootstrapServers());
        }
    }

    @Test
    public void testUsageWithVersion() throws Exception {
        try (KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))) {
            kafka.start();
            testKafkaFunctionality(
                    kafka.getBootstrapServers()
            );
        }
    }

    @Test
    public void testZookeeper() throws Exception {
        try (
                Network network = Network.newNetwork();
                KafkaContainer kafka = new KafkaContainer(KAFKA_TEST_IMAGE)
                        .withNetwork(network)
                        .withExternalZookeeper("zookeeper:2181");

                GenericContainer<?> zookeeper = new GenericContainer<>(ZOOKEEPER_TEST_IMAGE)
                        .withNetwork(network)
                        .withNetworkAliases("zookeeper")
                        .withEnv("ZOOKEEPER_CLIENT_PORT", "2181");
                GenericContainer<?> application = new GenericContainer<>(DockerImageName.parse("alpine"))
                        .withNetwork(network)
                        .withNetworkAliases("dummy")
                        .withCommand("sleep 10000")
        ) {
            zookeeper.start();
            kafka.start();
            application.start();

            testKafkaFunctionality(kafka.getBootstrapServers());
        }
    }

    @Test
    public void testConfluentPlatformVersion5() throws Exception {
        try (KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))) {
            kafka.start();
            testKafkaFunctionality(kafka.getBootstrapServers());
        }
    }

    @Test
    public void testWithHostExposedPort() throws Exception {
        Testcontainers.exposeHostPorts(12345);
        try (KafkaContainer kafka = new KafkaContainer(KAFKA_TEST_IMAGE)) {
            kafka.start();
            testKafkaFunctionality(kafka.getBootstrapServers());
        }
    }

    public void testKafkaFunctionality(String bootstrapServers) throws Exception {
        testKafkaFunctionality(bootstrapServers, 1, 1);
    }

    public void testKafkaFunctionality(String bootstrapServers, int partitions, int rf) throws Exception {
        try (
                AdminClient adminClient = AdminClient.create(
                        ImmutableMap.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
                );
                KafkaProducer<Long, User> producer = new KafkaProducer<>(producerConfigs(bootstrapServers));
                KafkaConsumer<Long, User> consumer = new KafkaConsumer<>(consumerConfigs(bootstrapServers));
        ) {
            String topicName = "msg";
            User user = new User();

            Collection<NewTopic> topics = Collections.singletonList(new NewTopic(topicName, partitions, (short) rf));
            adminClient.createTopics(topics).all().get(10, TimeUnit.SECONDS);

            consumer.subscribe(Collections.singletonList(topicName));

            producer.send(new ProducerRecord<Long, User>(topicName, 1L, user)).get();

            Unreliables.retryUntilTrue(
                    10,
                    TimeUnit.SECONDS,
                    () -> {
                        ConsumerRecords<Long, User> records = consumer.poll(Duration.ofMillis(100));

                        if (records.isEmpty()) {
                            return false;
                        }

                        assertThat(records)
                                .hasSize(1)
                                .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value);
                        return true;
                    }
            );
            consumer.unsubscribe();
        }
    }

    public Map<String, Object> consumerConfigs(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "todo");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");
        return props;
    }

    public Map<String, Object> producerConfigs(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }
}
