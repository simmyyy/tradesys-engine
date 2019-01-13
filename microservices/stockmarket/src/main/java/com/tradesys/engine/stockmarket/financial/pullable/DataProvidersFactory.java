package com.tradesys.engine.stockmarket.financial.pullable;

import com.tradesys.engine.stockmarket.financial.dpimpls.alphavantage.AlphaVantageFinancialDataProviderImpl;
import com.tradesys.engine.stockmarket.financial.dpimpls.coinapi.COINApiForeignExchangeDataProviderImpl;
import com.tradesys.engine.stockmarket.financial.dpimpls.iextrading.IEXTradingFinancialDataProviderImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProvidersFactory {

    AlphaVantageFinancialDataProviderImpl alphaVantageForeignExchangePullable;
    IEXTradingFinancialDataProviderImpl iexTradingFinancialDataPullable;
    COINApiForeignExchangeDataProviderImpl coinApiForeignExchangeDataProvider;

    public IFinancialDataPullable getFinancialDataPullableImpl(DataProvider dataProvider) {
        if (dataProvider.equals(DataProvider.ALPHAVANTAGE)) {
            return alphaVantageForeignExchangePullable;
        } else if (dataProvider.equals(DataProvider.IEXTrading)) {
            return iexTradingFinancialDataPullable;
        } else if (dataProvider.equals(DataProvider.COINApi)) {
            return coinApiForeignExchangeDataProvider;
        } else {
            throw new RuntimeException(dataProvider.toString() + " is not supported.");
        }
    }
}
