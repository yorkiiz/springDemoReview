package com.example.demo.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.iml.OrderItemMapper;
import com.example.demo.entity.iml.OrderMapper;
import com.example.demo.entity.iml.ProductMapper;
import com.example.demo.mq.CancelOrderSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final ProductService productService;
    private final CancelOrderSender cancelOrderSender;

    public OrderService(OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        ProductMapper productMapper,
                        ProductService productService,
                        CancelOrderSender cancelOrderSender) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.productService = productService;
        this.cancelOrderSender = cancelOrderSender;
    }

    @Transactional
    public Order createOrder(Long memberId, Long productId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (product.getStatus() != 1) {
            throw new RuntimeException("商品已下架");
        }

        boolean deductSuccess = productService.deductStock(productId, quantity);
        if (!deductSuccess) {
            throw new RuntimeException("库存不足");
        }

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        Order order = new Order();
        order.setOrderSn(generateOrderSn());
        order.setMemberId(memberId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setStatus(0);
        orderMapper.insert(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setOrderSn(order.getOrderSn());
        orderItem.setProductId(productId);
        orderItem.setProductName(product.getName());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItemMapper.insert(orderItem);

        cancelOrderSender.sendMessage(order.getId(), 30 * 60 * 1000L);

        return order;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            return;
        }

        order.setStatus(2);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);

        List<OrderItem> orderItems = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : orderItems) {
            productService.restoreStock(item.getProductId(), item.getQuantity());
        }
    }

    public List<Order> getMemberOrders(Long memberId) {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getMemberId, memberId)
                        .orderByDesc(Order::getCreateTime));
    }

    public Order getOrderDetail(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    private String generateOrderSn() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return timestamp + random;
    }
}
