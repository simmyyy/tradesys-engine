package com.tradesys.engine.stockmarket.financial;

import com.tradesys.engine.stockmarket.financial.pullableimpls.AlphaVantageFinancialDataPullableImpl;
import com.tradesys.engine.stockmarket.financial.pullableimpls.iextrading.IEXTradingFinancialDataPullableImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProvidersFactory {

    AlphaVantageFinancialDataPullableImpl alphaVantageForeignExchangePullable;
    IEXTradingFinancialDataPullableImpl iexTradingFinancialDataPullable;

    public IFinancialDataPullable getFinancialDataPullableImpl(DataProvider dataProvider) {
        if (dataProvider.equals(DataProvider.ALPHAVANTAGE)) {
            return alphaVantageForeignExchangePullable;
        } else if (dataProvider.equals(DataProvider.IEXTrading)) {
            return iexTradingFinancialDataPullable;
        } else {
            throw new RuntimeException(dataProvider.toString() + " is not supported.");
        }
    }

}
