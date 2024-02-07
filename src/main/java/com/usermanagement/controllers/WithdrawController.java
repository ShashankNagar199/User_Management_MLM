package com.usermanagement.controllers;

import com.usermanagement.dto.WithdrawalRequestDto;
import com.usermanagement.services.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/withdraw")
public class WithdrawController {
    @Autowired
    private WithdrawService withdrawService;

    @PostMapping(value="/tronAmount")
    public String withdrawAmount(@RequestBody WithdrawalRequestDto withdrawalRequest){
        String fromAddress = withdrawalRequest.getFromAddress();
        String toAddress = withdrawalRequest.getToAddress();
        String amount = withdrawalRequest.getAmount();

        return withdrawService.withdrawTron(fromAddress,toAddress,amount);
    }
}
