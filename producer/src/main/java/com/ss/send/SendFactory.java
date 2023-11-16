package com.ss.send;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class SendFactory {

    public String msg;
    public String name;
    public static final String EXCHANGE_NAME="amq.direct";
    public static String BIND;

    public SendFactory(String msg,String MyName){
        this.msg=msg;
        this.name=MyName;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("114.132.150.220");//?
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("Zhao_wanru");
        connectionFactory.setPassword("wanru");
        connectionFactory.setVirtualHost("Zhao_wanru");
        Connection connection = null;
        Channel channel = null;
        BIND=MyName;
        try{
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .contentType("text/plain")
                    .contentEncoding("UTF-8")
                    .appId(name)
                    .messageId("非文件")
                    .deliveryMode(2)
                    .build();
            channel.basicPublish(EXCHANGE_NAME,BIND,properties,this.msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("send success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
