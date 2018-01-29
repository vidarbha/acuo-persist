package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class AlgoError extends ServiceError {

    @Relationship(type = "ALGO_ERROR")
    private MarginStatement statement;

    public AlgoError() {
    }

    public AlgoError(com.acuo.common.model.results.AlgoError model) {
        setCode(model.getCode());
        setMessage(model.getMessage());
        setDateTime(model.getDateTime());
    }

    public com.acuo.common.model.results.AlgoError model() {
        com.acuo.common.model.results.AlgoError error = new com.acuo.common.model.results.AlgoError();
        error.setCode(getCode());
        error.setMessage(getMessage());
        error.setDateTime(getDateTime());
        return error;
    }
}
