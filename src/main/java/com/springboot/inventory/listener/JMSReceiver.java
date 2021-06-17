package com.springboot.inventory.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import com.springboot.inventory.config.JMSConfiguration;
import com.springboot.inventory.dto.InventoryTable;
import com.springboot.inventory.model.Inventory;
import com.springboot.inventory.model.Order;
import com.springboot.inventory.service.InventoryService;
import com.springboot.inventory.util.JsonUtils;

import io.vavr.collection.Queue;

@Component
public class JMSReceiver implements SessionAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(JMSReceiver.class);

    @Autowired
    InventoryService inventoryService;
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    private static final String INVENTORY_QUEUE = "inventoryQueue";
   
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        String txt = ((TextMessage) message).getText();
        logger.info("JMS Text Message received: " + txt);
        Order order = JsonUtils.read(txt, Order.class);
        listenForOrderEvent(txt);
    }
   
    
    public void listenForOrderEvent(String message) {
        Order order = JsonUtils.read(message, Order.class);

		logger.info("orderId:" +order.getOrderid());
		logger.info("itemId:" +order.getItemid());
		
		String location= evaluateInventory(order);
		inventoryEvent(order.getOrderid(), order.getItemid(), location);
        System.out.println("Received Message: " + message);
    }
	
	
	public void inventoryEvent(String orderId, String itemId, String location) {
		
		Inventory inventory= new Inventory(orderId, itemId, location, "beer");
        String jsonString = JsonUtils.writeValueAsString(inventory);
     
        jmsTemplate.convertAndSend(INVENTORY_QUEUE, jsonString);

	}
	
    public String evaluateInventory(Order order) {
    	String itemId= order.getItemid();
    	inventoryService.removeInventory(itemId);
    	InventoryTable viewInventory= inventoryService.viewInventory(itemId);
    	String inventoryLocation= viewInventory!=null? viewInventory.getInventoryLocation() : "inventoryDoesNotExist";
    	
    	logger.info("InventoryServiceOrderEventConsumer orderId:" +order.getOrderid());
		logger.info("itemId:" +order.getItemid());
		return inventoryLocation;
    }
}
