package com.tradesys.engine.stockmarket.financial.repository;


import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.writable.DownstreamSystem;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//currently data is hardcoded but DB may be used as well
@Component
public class PullableMetadataRepository {

    static private List<PullableMetadata> pullableMetadataList = Collections.synchronizedList(new ArrayList<>());

    static {
        pullableMetadataList.add(new PullableMetadata(1L, DataProvider.ALPHAVANTAGE, 61000L, "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s", "sample description", "sample ", "fxrate", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(2L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/ref-data/symbols", "IEXTrading-Symbols", "sample ", "ref_data", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(3L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/company", "sample description", "sample ", "company", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(4L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/chart/5y", "sample description", "sample ", "stock_chart", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(5L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/stock/{symbol}/chart/6m", "sample description", "sample ", "stock_chart", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(6L, DataProvider.IEXTrading, 301000L, "https://api.iextrading.com/1.0/stock/{symbol}/chart/dynamic", "sample description", "sample ", "stock_chart", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(7L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/earnings", "sample description", "sample ", "earnings", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(8L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/market/today-earnings", "sample description", "sample ", "earnings", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(9L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/financials", "sample description", "sample ", "financial", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(10L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/stock/market/sector-performance", "sample description", "sample ", "sector_performance", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(11L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/stock/{symbol}/news/last/5", "sample description", "sample ", "news", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(12L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/dividends/5y", "sample description", "sample ", "dividends", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(13L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/price", "sample description", "sample ", "stock_price", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(14L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/market", "sample description", "sample ", "market", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(15L, DataProvider.IEXTrading, 0L, "https://api.iextrading.com/1.0/stock/{symbol}/quote", "sample description", "sample ", "quote", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(16L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/stock/market/crypto", "sample description", "sample ", "crypto", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(17L, DataProvider.COINApi, 0L, "https://rest.coinapi.io/v1/exchangerate/%s", "sample description", "sample ", "fxrate", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(18L, DataProvider.ALPHAVANTAGE, 0L, "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol={symbol}&interval=1min&apikey=%s", "sample description", "sample ", "stock_chart", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(19L, DataProvider.ALPHAVANTAGE, 0L, "https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=%s&to_symbol=%s&interval=1min&apikey=%s", "sample description", "sample ", "fxrate", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(20L, DataProvider.ALPHAVANTAGE, 0L, "https://www.alphavantage.co/query?function=FX_DAILY&from_symbol={fromCurrency}&to_symbol={toCurrency}&apikey=%s", "sample description", "sample", "fxrate", 1, DownstreamSystem.KAFKA));
        pullableMetadataList.add(new PullableMetadata(21L, DataProvider.ALPHAVANTAGE, 61000L, "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol={fromSymbol}&market={toMarket}&apikey=%s", "sample description", "sample", "fxrate", 1, DownstreamSystem.KAFKA));
    }

    public List<PullableMetadata> getAllByProvider(DataProvider dataProvider) {
        return pullableMetadataList.stream().filter(s -> s.getDataProvider().equals(dataProvider)).collect(Collectors.toList());
    }

    public PullableMetadata getByProcessId(Long id) {
        return pullableMetadataList.stream().filter(s -> s.getProcessId().equals(id)).findFirst().orElseThrow(() -> new ProcessNotFoundException("Can't find process with id: " + id));
    }

}
