package com.springboot.inventory.config;

import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oracle.jms.AQjmsFactory;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jms.AQjmsFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class OracleAQConfiguration {


    @Value("${oracle.username}")
    private String user;

    @Value("${oracle.password}")
    private String password;

    @Value("${oracle.url}")
    private String url;

    @Bean
    public DataSource dataSource() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setUser(user);
        ds.setPassword(password);
        ds.setURL(url);
        ds.setImplicitCachingEnabled(true);
        ds.setFastConnectionFailoverEnabled(true);
        return ds;
    }

    @Bean
    public QueueConnectionFactory connectionFactory(DataSource dataSource) throws JMSException {
        return AQjmsFactory.getQueueConnectionFactory(dataSource);
    }

}