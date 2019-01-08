package com.tradesys.engine.stockmarket.fxservice;

import lombok.Getter;

@Getter
public enum ForeignExchangeDataProviders {

    ALPHAVANTAGE(10);

    private int priority;

    ForeignExchangeDataProviders(int priority) {
        this.priority = priority;
    }

}
