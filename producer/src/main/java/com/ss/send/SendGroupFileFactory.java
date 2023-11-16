package com.ss.send;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ss.tools.file.ReadFile;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class SendGroupFileFactory {
    public String msg;
    public String name;
    public static final String EXCHANGE_NAME="amq.fanout";
    public SendGroupFileFactory(String msg,String username) throws FileNotFoundException {
        this.msg = msg;
        this.name=username;
        ReadFile readFile=new ReadFile(msg);
        byte[]body=readFile.body;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("114.132.150.220");//?
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("Zhao_wanru");
        connectionFactory.setPassword("wanru");
        connectionFactory.setVirtualHost("Zhao_wanru");
        Connection connection = null;
        Channel channel = null;
        try{
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout",true);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .contentType("text/plain")
                    .contentEncoding("UTF-8")
                    .appId(name)
                    .messageId("文件")
                    .deliveryMode(2)
                    .build();
            channel.basicPublish(EXCHANGE_NAME,"",properties,body);
            System.out.println("send success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
