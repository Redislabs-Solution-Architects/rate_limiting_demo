package com.bestarch.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestarch.demo.domain.Prospect;
import com.bestarch.demo.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerAPIController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = {"/prospects"})
    public ResponseEntity<List<Prospect>> prospects() {
        List<Prospect> prospects = new ArrayList<>();
        prospects.addAll(customerService.getProspects());
        return ResponseEntity.ok().body(prospects);
    }

}