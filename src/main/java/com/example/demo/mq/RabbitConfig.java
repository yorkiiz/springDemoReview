package com.example.demo.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 定义队列，队列名称：hello_queue
    @Bean
    public Queue helloQueue() {
        // 参数1：队列名；参数2：durable 是否持久化（重启MQ队列不丢失）
        return new Queue("hello_queue", true);
    }
}

