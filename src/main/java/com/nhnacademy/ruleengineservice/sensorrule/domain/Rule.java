package com.nhnacademy.ruleengineservice.sensorrule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.DataType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    private DataType datatype;
    private Operator operator;
    private Double value;
    private Double range;
    private ActionType action;

    public static Rule createRule(DataType datatype, Operator operator, Double value, Double range, ActionType action) {
        return Rule.builder()
                .datatype(datatype)
                .operator(operator)
                .value(value)
                .range(range)
                .action(action)
                .build();
    }
}