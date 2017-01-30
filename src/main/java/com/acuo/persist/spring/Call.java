package com.acuo.persist.spring;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
public class Call {
    private String category;
    private String type;
    private String status;
    private Double balance;
    private Double excess;
}