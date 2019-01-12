package com.tradesys.engine.stockmarket.engine;


import com.tradesys.engine.stockmarket.financial.DataProvidersFactory;
import com.tradesys.engine.stockmarket.utils.Currency;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
/**
 * Class under development
 */
public class ForeignExchangeDataPullingEngine {

    private final DataProvidersFactory foreignExchangeDataProvidersFactory;

    private static final Currency GLOBAL_TO_CURRENCY = Currency.USD;

    public List<Currency> getForeignExchangePairs() {
        return Arrays.stream(Currency.values())
                .filter(s -> !s.equals(GLOBAL_TO_CURRENCY))
                .collect(Collectors.toList());
    }

//    public List<ForeignExchangeRateInfo> pullForeignExchangeRateInfo(DataProvider dataProvider) {
//        IFinancialDataPullable pullable = foreignExchangeDataProvidersFactory.getFinancialDataPullableImpl(dataProvider);
//        List<ForeignExchangeRateInfo> ls = getForeignExchangePairs()
//                .parallelStream()
//                .map(k -> pullable.pull())
//                .map(opt -> opt.get())
//                .map(res -> pullable.toForeignExchangeRateInfo(res))
//                .map(optRateInfo -> (ForeignExchangeRateInfo) optRateInfo.orElseGet(ForeignExchangeRateInfo::new))
//                .collect(Collectors.toList());
//        return ls;
//    }

}
