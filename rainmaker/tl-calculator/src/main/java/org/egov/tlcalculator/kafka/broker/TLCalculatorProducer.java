package org.egov.tlcalculator.kafka.broker;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TLCalculatorProducer {
	
	@Autowired
	private LogAwareKafkaTemplate kafkaTemplate;
	
	/**
	 * Listener method to push records to kafka queue.
	 * @param topic
	 * @param value
	 */
	public void push(String topic, Object value) {
		kafkaTemplate.send(topic, value);
	}

}
