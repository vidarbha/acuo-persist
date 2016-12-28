package com.acuo.persist.spring;

import lombok.Data;

//@QueryResult
@Data
public class Call {
    private String category;
    private String type;
    private String status;
    private Double balance;
    private Double excess;
}