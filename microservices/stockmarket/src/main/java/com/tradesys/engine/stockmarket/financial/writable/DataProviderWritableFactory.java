package com.tradesys.engine.stockmarket.financial.writable;

import com.tradesys.engine.stockmarket.financial.dpimpls.alphavantage.AlphaVantageFinancialDataProviderImpl;
import com.tradesys.engine.stockmarket.financial.dpimpls.coinapi.COINApiForeignExchangeDataProviderImpl;
import com.tradesys.engine.stockmarket.financial.dpimpls.iextrading.IEXTradingFinancialDataProviderImpl;
import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataProviderWritableFactory {

    private final AlphaVantageFinancialDataProviderImpl alphaVantageFinancialDataProvider;
    private final IEXTradingFinancialDataProviderImpl iexTradingFinancialDataProvider;
    private final COINApiForeignExchangeDataProviderImpl coinApiForeignExchangeDataProvider;

    public IFinancialDataWritable getDataProviderWritableImpl(DataProvider dataProvider, DownstreamSystem downstreamSystem) {
        if (dataProvider.equals(DataProvider.IEXTrading) && downstreamSystem.equals(DownstreamSystem.KAFKA)) {
            return iexTradingFinancialDataProvider;
        } else if (dataProvider.equals(DataProvider.ALPHAVANTAGE) && downstreamSystem.equals(DownstreamSystem.KAFKA)) {
            return alphaVantageFinancialDataProvider;
        } else if (dataProvider.equals(DataProvider.COINApi) && downstreamSystem.equals(DownstreamSystem.KAFKA)) {
            return coinApiForeignExchangeDataProvider;
        } else {
            throw new RuntimeException("Cannot get writable instance for data provider: " + dataProvider.toString() + " and downstream system: " + downstreamSystem.toString());
        }
    }
}