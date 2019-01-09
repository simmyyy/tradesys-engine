package com.tradesys.engine.stockmarket.fxservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ForeignExchangeDataProvidersFactory {

    AlphaVantageForeignExchangePullableImpl alphaVantageForeignExchangePullable;

    public IForeignExchangePullable<?> getForeignExchangePullableImpl(ForeignExchangeDataProvider foreignExchangeDataProvider) {
        if (foreignExchangeDataProvider.equals(ForeignExchangeDataProvider.ALPHAVANTAGE)) {
            return alphaVantageForeignExchangePullable;
        } else {
            throw new RuntimeException(foreignExchangeDataProvider.toString() + " is not supported.");
        }
    }

}
