package com.tradesys.engine.stockmarket.engine;


import com.tradesys.engine.stockmarket.fxservice.ForeignExchangeDataProvider;
import com.tradesys.engine.stockmarket.fxservice.ForeignExchangeDataProvidersFactory;
import com.tradesys.engine.stockmarket.fxservice.IForeignExchangePullable;
import com.tradesys.engine.stockmarket.utils.Currency;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
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

    private final ForeignExchangeDataProvidersFactory foreignExchangeDataProvidersFactory;

    private static final Currency GLOBAL_TO_CURRENCY = Currency.USD;

    public List<Currency> getForeignExchangePairs() {
        return Arrays.stream(Currency.values())
                .filter(s -> !s.equals(GLOBAL_TO_CURRENCY))
                .collect(Collectors.toList());
    }

    public List<ForeignExchangeRateInfo> pullForeignExchangeRateInfo(ForeignExchangeDataProvider dataProvider) {
        IForeignExchangePullable pullable = foreignExchangeDataProvidersFactory.getForeignExchangePullableImpl(dataProvider);
        List<ForeignExchangeRateInfo> ls = getForeignExchangePairs()
                .parallelStream()
                .map(k -> pullable.pullCurrencyExchangeRate(GLOBAL_TO_CURRENCY, k))
                .map(opt -> opt.get())
                .map(res -> pullable.toForeignExchangeRateInfo(res))
                .map(optRateInfo -> (ForeignExchangeRateInfo) optRateInfo.orElseGet(ForeignExchangeRateInfo::new))
                .collect(Collectors.toList());
        return ls;
    }

}
