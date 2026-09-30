package com.eatrading.api.controller;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.eatrading.api.dto.OrderTransactionRequest;

public class KafkaPipelineListener {

    private static final String BROKER = "localhost:9092";

    private volatile boolean running = true;
    private Thread validationThread;
    private Thread processingThread;

    public static void main(String[] args) {
        KafkaPipelineListener pipeline = new KafkaPipelineListener();

        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "pipeline-shutdown"));

        pipeline.start();

        System.out.println("Kafka pipeline running. Press Ctrl+C to stop.");
    }

    public void start() {
        validationThread = new Thread(
                () -> runStage(
                        "orders.incoming",
                        "orders.unvalidated",
                        "order-validator"),
                "order-validation");

        processingThread = new Thread(
                () -> runStage(
                        "orders.unvalidated",
                        "orders.unprocessed",
                        "order-processor"),
                "order-processing");

        validationThread.start();
        processingThread.start();
    }

    public void stop() {
        running = false;

        if (validationThread != null) {
            validationThread.interrupt();
        }

        if (processingThread != null) {
            processingThread.interrupt();
        }
    }

    private void runStage(String inputTopic, String outputTopic, String groupId) {
    	System.out.println("runStage");
    	Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        JsonDeserializer<OrderTransactionRequest> jsonDeserializer =
                new JsonDeserializer<>(OrderTransactionRequest.class);
        jsonDeserializer.addTrustedPackages(
                OrderTransactionRequest.class.getPackageName());

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", BROKER);

        try (KafkaConsumer<String, OrderTransactionRequest> consumer =
                     new KafkaConsumer<>(
                             consumerProps,
                             new StringDeserializer(),
                             jsonDeserializer);
             KafkaProducer<String, OrderTransactionRequest> producer =
                     new KafkaProducer<>(
                             producerProps,
                             new StringSerializer(),
                             new JsonSerializer<>())) {

            consumer.subscribe(List.of(inputTopic));
            System.out.println("Listening on " + inputTopic);

            while (running && !Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, OrderTransactionRequest> records =
                        consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, OrderTransactionRequest> record : records) {
                    OrderTransactionRequest request = record.value();

                    System.out.println(
                            "Received order " + request.TRACKING_ID
                                    + " from " + inputTopic);

                    Thread.sleep(10_000);

                    producer.send(new ProducerRecord<>(
                            outputTopic,
                            record.key(),
                            request
                    )).get();

                    System.out.println(
                            "Sent order " + request.TRACKING_ID
                                    + " to " + outputTopic);
                }

                if (!records.isEmpty()) {
                    consumer.commitSync();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            if (running) {
                System.out.println("Pipeline stage failed: " + inputTopic);
                e.printStackTrace();
            }
        }
    }
}