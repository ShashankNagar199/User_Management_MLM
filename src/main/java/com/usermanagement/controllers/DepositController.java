package com.usermanagement.controllers;

import com.usermanagement.services.UserService;
import com.usermanagement.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/deposit")
public class DepositController {
    @Autowired
    private  WalletService walletService;

//    @PostMapping(value="/usdtAmount/{address}")
//    public String depositAmount(@PathVariable String address){
//        walletService.sendEther(address);
//    }
}
