package com.java25.java25.springboot4.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiEventLog {
    private String endpoint;
    private String httpMethod;
    private Object payload;
    private long timestamp;
}
