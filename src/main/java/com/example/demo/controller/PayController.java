package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.PayDTO;
import com.example.demo.entity.PayRecord;
import com.example.demo.user.PayService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping("/submit")
    public Result<PayRecord> pay(@Valid @RequestBody PayDTO dto) {
        PayRecord payRecord = payService.pay(dto.getOrderId(), dto.getPayType());
        return Result.success("支付成功", payRecord);
    }
}
