package com.sileyouhe.mallportal.dto;

import lombok.Getter;

@Getter
public enum QueueEnum {

    // message
    QUEUE_ORDER_CANCEL("mall.order.direct", "mall.order.cancel", "mall.order.cancel"),

    // message TTL
    QUEUE_TTL_ORDER_CANCEL("mall.order.direct.ttl", "mall.order.cancel.ttl", "mall.order.cancel.ttl");

    // exchange
    private String exchange;

    // queue name
    private String name;

    // routeKey
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
