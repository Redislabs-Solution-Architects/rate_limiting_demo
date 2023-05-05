package com.bestarch.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prospect {
    private String id;
    private String contactNo;
    private String address;
    private String email;
    private Integer creditScore;
    private Double liability;
    private Integer defaults;
    private String requirement;
}