package com.example.demo.mq;

import com.example.demo.user.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "demo.order.cancel")
public class CancelOrderReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);

    private final OrderService orderService;

    public CancelOrderReceiver(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitHandler
    public void handle(Long orderId) {
        LOGGER.info("收到超时取消订单消息, orderId:{}", orderId);
        try {
            orderService.cancelOrder(orderId);
            LOGGER.info("订单超时自动取消成功, orderId:{}", orderId);
        } catch (Exception e) {
            LOGGER.error("订单超时自动取消失败, orderId:{}, error:{}", orderId, e.getMessage());
        }
    }
}
