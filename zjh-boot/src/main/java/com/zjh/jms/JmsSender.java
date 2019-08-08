package com.zjh.jms;
/*
import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsSender {
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private Topic topic;
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	public void sendByQueue(String message) {
		this.jmsMessagingTemplate.convertAndSend(queue, message);
	}
	
	public void sendByTolic(String message) {
		this.jmsMessagingTemplate.convertAndSend(topic, message);
	}

}
*/