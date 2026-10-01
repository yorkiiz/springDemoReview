package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderSn;
    private Long memberId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer payType;
    private Integer status;
    private LocalDateTime payTime;
    private LocalDateTime cancelTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
