package com.example.demo.user;

import com.example.demo.entity.Order;
import com.example.demo.entity.PayRecord;
import com.example.demo.entity.iml.OrderMapper;
import com.example.demo.entity.iml.PayRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PayService {

    private final OrderMapper orderMapper;
    private final PayRecordMapper payRecordMapper;

    public PayService(OrderMapper orderMapper, PayRecordMapper payRecordMapper) {
        this.orderMapper = orderMapper;
        this.payRecordMapper = payRecordMapper;
    }

    @Transactional
    public PayRecord pay(Long orderId, Integer payType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            if (order.getStatus() == 1) {
                throw new RuntimeException("订单已支付");
            } else if (order.getStatus() == 2) {
                throw new RuntimeException("订单已取消");
            }
            throw new RuntimeException("订单状态异常，无法支付");
        }

        PayRecord payRecord = new PayRecord();
        payRecord.setOrderId(orderId);
        payRecord.setOrderSn(order.getOrderSn());
        payRecord.setTradeNo(generateTradeNo());
        payRecord.setPayAmount(order.getPayAmount());
        payRecord.setPayType(payType);
        payRecord.setPayStatus(0);
        payRecordMapper.insert(payRecord);

        boolean paySuccess = simulatePay(payType, order.getPayAmount());

        if (paySuccess) {
            payRecord.setPayStatus(1);
            payRecord.setUpdateTime(LocalDateTime.now());
            payRecordMapper.updateById(payRecord);

            order.setStatus(1);
            order.setPayType(payType);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);

            return payRecord;
        } else {
            payRecord.setPayStatus(2);
            payRecord.setUpdateTime(LocalDateTime.now());
            payRecordMapper.updateById(payRecord);
            throw new RuntimeException("支付失败");
        }
    }

    private boolean simulatePay(Integer payType, java.math.BigDecimal amount) {
        return ThreadLocalRandom.current().nextInt(100) < 95;
    }

    private String generateTradeNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "PAY" + timestamp + random;
    }
}
