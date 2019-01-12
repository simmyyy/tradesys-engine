package com.tradesys.engine.stockmarket.financial.repository;


import com.tradesys.engine.stockmarket.financial.DataProvider;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
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
        pullableMetadataList.add(new PullableMetadata(1L, DataProvider.ALPHAVANTAGE, 30000L, "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s", "sample description", "sample "));
        pullableMetadataList.add(new PullableMetadata(2L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/ref-data/symbols", "sample description", "sample "));
        pullableMetadataList.add(new PullableMetadata(3L, DataProvider.IEXTrading, 30000L, "https://api.iextrading.com/1.0/stock/{symbol}/company", "sample description", "sample "));
    }

    public List<PullableMetadata> getAllByProvider(DataProvider dataProvider) {
        return pullableMetadataList.stream().filter(s -> s.getDataProvider().equals(dataProvider)).collect(Collectors.toList());
    }

    public PullableMetadata getByProcessId(Long id) {
        return pullableMetadataList.stream().filter(s -> s.getProcessId().equals(id)).findFirst().get();
    }

}
