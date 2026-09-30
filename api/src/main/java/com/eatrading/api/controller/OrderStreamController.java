package com.eatrading.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatrading.api.dto.OrderTransactionRequest;

@RestController
@RequestMapping("/stream")
public class OrderStreamController {

	@Autowired
	private KafkaTemplate<String, OrderTransactionRequest> kafkaTemplate;

	@GetMapping("/orders")
	public long createTransactionOrder(OrderTransactionRequest request) throws Exception {

		kafkaTemplate.send("orders.incoming", String.valueOf(request.TRACKING_ID), request).get();
		return request.TRACKING_ID;
		
	}
}

/*
 * System.out.println(request.TRACKING_ID + "w" ); Properties props = new
 * Properties(); props.put("bootstrap.servers", "http://127.0.0.1:9092");
 * props.put("key.serializer",
 * "org.apache.kafka.common.serialization.StringSerializer");
 * props.put("value.serializer",
 * "org.springframework.kafka.support.serializer.JsonSerializer"); try {
 * KafkaProducer<String, OrderTransactionRequest> producer = new
 * KafkaProducer<>(props); ProducerRecord<String, OrderTransactionRequest>
 * record = new ProducerRecord<>("orders.incoming", ""+request.TRACKING_ID,
 * request); RecordMetadata metadata = producer.send(record).get();
 * System.out.printf("partition=%d offset=%d%n", metadata.partition(),
 * metadata.offset()); producer.close(); } catch (Exception e) {
 * e.printStackTrace(); }
 */