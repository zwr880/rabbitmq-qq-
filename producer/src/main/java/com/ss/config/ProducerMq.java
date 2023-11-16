package com.ss.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerMq {

    public static final String EXCHANGE_NAME="amq.topic";
    public static final String QUEUE_NAME="zwr.queue";

    //交换机 amq.queue
    @Bean("topic")
    public Exchange topicExchange(){

        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }
    //队列 zwr.queue
    @Bean("zwr")
    public Queue topicQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    //绑定
    public Binding bindQueueExchange(@Qualifier("zwr") Queue queue,@Qualifier("topic") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("ww").noargs();
    }
}
