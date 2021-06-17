package com.springboot.inventory.config;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.springboot.inventory.listener.JMSReceiver;

@Configuration
public class JMSConfiguration {
    private static final String ORDER_QUEUE = "orderQueue";
    private static final String INVENTORY_QUEUE = "inventoryQueue";

    @Autowired
    private JMSReceiver jmsReceiver;

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory conFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setDefaultDestinationName(INVENTORY_QUEUE);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setConnectionFactory(conFactory);

        return jmsTemplate;
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(ConnectionFactory conFactory, DataSource dataSource) {
        DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
        dmlc.setDestinationName(ORDER_QUEUE);
        dmlc.setSessionTransacted(true);
        dmlc.setConnectionFactory(conFactory);

        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        dmlc.setTransactionManager(manager);

        
        dmlc.setMessageListener(jmsReceiver);
        return dmlc;
    }

}

