package com.tradesys.engine.stockmarket.financial;

import com.tradesys.engine.stockmarket.financial.pullableimpls.AlphaVantageFinancialDataPullableImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProvidersFactory {

    AlphaVantageFinancialDataPullableImpl alphaVantageForeignExchangePullable;

    public IFinancialDataPullable getFinancialDataPullableImpl(DataProvider dataProvider) {
        if (dataProvider.equals(DataProvider.ALPHAVANTAGE)) {
            return alphaVantageForeignExchangePullable;
        } else {
            throw new RuntimeException(dataProvider.toString() + " is not supported.");
        }
    }

}
