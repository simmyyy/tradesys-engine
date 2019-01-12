package com.tradesys.engine.stockmarket.financial.pullable;

import lombok.Getter;

@Getter
public enum DataProvider {

    ALPHAVANTAGE(10), IEXTrading(9);

    private int priority;

    DataProvider(int priority) {
        this.priority = priority;
    }

}
