package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String orderSn;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
