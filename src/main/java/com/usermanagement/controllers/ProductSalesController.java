package com.usermanagement.controllers;

import com.usermanagement.dto.ProductSalesParamsdto;
import com.usermanagement.models.SalesRecord;
import com.usermanagement.services.ProductSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductSalesController {
    @Autowired
    private ProductSalesService productSalesService;

    @PostMapping("/product")
    public ResponseEntity<SalesRecord> addProduct(@RequestBody ProductSalesParamsdto productSalesParamsdto) throws Exception {
        return new ResponseEntity<>(productSalesService.addProductSales(productSalesParamsdto.getProduct(),productSalesParamsdto.getUserId()), HttpStatus.CREATED);
    }

}
