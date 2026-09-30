package com.example.demo.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "demo.order.cancel")
public class CancelOrderReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);

    @RabbitHandler
    public void handle(Long orderId) {
        LOGGER.info("收到超时取消订单消息, orderId:{}", orderId);
        // TODO: 调用订单服务，执行取消订单、释放库存、退还优惠券等操作
        System.out.println("【订单超时取消】订单号：" + orderId + " 已自动取消");
    }
}
