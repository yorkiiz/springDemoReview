package com.example.demo.mq;

import lombok.Getter;

@Getter
public enum QueueEnum {
    QUEUE_ORDER_CANCEL("demo.order.direct", "demo.order.cancel", "demo.order.cancel"),
    QUEUE_TTL_ORDER_CANCEL("demo.order.direct.ttl", "demo.order.cancel.ttl", "demo.order.cancel.ttl");

    private final String exchange;
    private final String name;
    private final String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
