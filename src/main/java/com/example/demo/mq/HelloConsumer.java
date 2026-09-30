package com.example.demo.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloConsumer {

    // 监听 hello_queue 队列，有消息自动执行
    @RabbitListener(queues = "hello_queue")
    public void consume(String message) throws Exception {
        Thread.sleep(100);
        System.out.println("【消费者收到消息】：" + message);
        System.out.println("【消费消息成功】" + message);
        //throw new Exception("测试消费失败");
    }
}
