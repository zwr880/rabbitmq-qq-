package com.ss.send;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


public class SendGroupFactory {
    public String msg;
    public String name;
    public static final String EXCHANGE_NAME="amq.fanout";

    public SendGroupFactory(String msg,String username) {
        this.msg = msg;
        this.name=username;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("114.132.150.220");//?
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("Zhao_wanru");
        connectionFactory.setPassword("wanru");
        connectionFactory.setVirtualHost("Zhao_wanru");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .contentType("text/plain")
                    .contentEncoding("UTF-8")
                    .appId(name)
                    .messageId("非文件")
                    .deliveryMode(2)
                    .build();
            channel.basicPublish(EXCHANGE_NAME, "", properties, this.msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("send success");
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
