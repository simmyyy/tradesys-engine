package com.tradesys.engine.stockmarket.fxservice;

import com.tradesys.engine.stockmarket.utils.Currency;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;

import java.util.Optional;

public interface IForeignExchangePullable<T> {

    Optional<T> pullCurrencyExchangeRate(final Currency fromCurrency, final Currency toCurrency);

    Optional<ForeignExchangeRateInfo> toForeignExchangeRateInfo(T ForeignExchangeRate);

}
