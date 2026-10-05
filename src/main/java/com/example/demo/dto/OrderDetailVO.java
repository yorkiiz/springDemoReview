package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    private Long id;
    private String orderSn;
    private Long memberId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer payType;
    private Integer status;
    private LocalDateTime payTime;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<OrderItemVO> items;

    @Data
    public static class OrderItemVO {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal productPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}