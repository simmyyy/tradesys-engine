package com.tradesys.engine.stockmarket.financial.pullable;

import lombok.Getter;

@Getter
public enum DataProvider {

    ALPHAVANTAGE(100), IEXTrading(60), COINApi(80);

    private int priority;

    DataProvider(int priority) {
        this.priority = priority;
    }

}
