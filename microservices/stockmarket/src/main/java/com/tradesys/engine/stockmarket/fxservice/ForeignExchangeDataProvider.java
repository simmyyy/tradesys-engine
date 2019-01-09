package com.tradesys.engine.stockmarket.fxservice;

import lombok.Getter;

@Getter
public enum ForeignExchangeDataProvider {

    ALPHAVANTAGE(10);

    private int priority;

    ForeignExchangeDataProvider(int priority) {
        this.priority = priority;
    }

}
