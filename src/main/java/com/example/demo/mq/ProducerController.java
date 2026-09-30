package com.example.demo.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendMsg() {
        String msg = "Hello RabbitMQ，第一条消息";
        // 参数1：队列名；参数2：消息内容
        rabbitTemplate.convertAndSend("hello_queue", msg);
        return "消息发送成功：" + msg;
    }

    // 路径变量 {n}，方法参数接收 Integer n
    @GetMapping("/sendn/{n}")
    public String sendnMsg(@PathVariable Integer n) {
        for (int i = 1; i <= n; i++) {
            String msg = "Hello RabbitMQ，第" + i + "条消息";
            rabbitTemplate.convertAndSend("hello_queue", msg);
        }
        return "成功发送 " + n + " 条消息";
    }
}

