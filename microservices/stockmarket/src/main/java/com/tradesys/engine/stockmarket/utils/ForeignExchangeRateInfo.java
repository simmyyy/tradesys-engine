package com.tradesys.engine.stockmarket.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ForeignExchangeRateInfo {

    private Currency fromCurrency;
    private Currency toCurrency;
    private BigDecimal exchangeRate;
    private ZonedDateTime zonedDateTime;

    public ForeignExchangeRateInfo() {

    }

}
