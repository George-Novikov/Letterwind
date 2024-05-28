package com.georgen.letterwind.broker.handlers;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;

public abstract class EventHandler<T> extends MessageConveyor<T> {
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
