package com.example.demo.controller;

import com.example.demo.mq.QueueEnum;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

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

    @GetMapping("/order/create/{orderId}")
    public String createOrder(@PathVariable Long orderId) {
        long delayTimes = 50 * 1000; // 测试用10秒，正式改为 30 * 60 * 1000
        amqpTemplate.convertAndSend(
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
                orderId,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                        return message;
                    }
                });
        return "下单成功，订单号：" + orderId + "，" + (delayTimes / 1000) + "秒后若未支付将自动取消";
    }
}

