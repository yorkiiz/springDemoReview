package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.iml.MemberMapper;
import com.example.demo.entity.Member;
import com.example.demo.user.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberMapper memberMapper;

    public OrderController(OrderService orderService, MemberMapper memberMapper) {
        this.orderService = orderService;
        this.memberMapper = memberMapper;
    }

    @PostMapping("/create")
    public Result<Order> create(@Valid @RequestBody OrderCreateDTO dto) {
        Long memberId = getCurrentMemberId();
        Order order = orderService.createOrder(memberId, dto.getProductId(), dto.getQuantity());
        return Result.success("订单创建成功", order);
    }

    @PostMapping("/cancel/{orderId}")
    public Result<String> cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return Result.success("订单取消成功", null);
    }

    @GetMapping("/list")
    public Result<List<Order>> list() {
        Long memberId = getCurrentMemberId();
        List<Order> orders = orderService.getMemberOrders(memberId);
        return Result.success(orders);
    }

    @GetMapping("/detail/{orderId}")
    public Result<Order> detail(@PathVariable Long orderId) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            return Result.fail(404, "订单不存在");
        }
        return Result.success(order);
    }

    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberMapper.selectOne(
                new LambdaQueryWrapper<Member>().eq(Member::getUsername, username));
        if (member == null) {
            throw new RuntimeException("用户不存在");
        }
        return member.getId();
    }
}
