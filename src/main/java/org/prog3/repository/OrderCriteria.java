package org.prog3.repository;

import org.prog3.model.Order;

public class OrderCriteria {
    private String column;

    public Order getOrder() {
        return order;
    }

    public String getColumn() {
        return column;
    }

    private Order order;

    public OrderCriteria(String column, Order order) {
        this.column = column;
        this.order = order;
    }

}

