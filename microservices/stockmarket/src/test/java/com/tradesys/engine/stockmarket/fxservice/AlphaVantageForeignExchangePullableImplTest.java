package com.tradesys.engine.stockmarket.fxservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradesys.engine.stockmarket.utils.Currency;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlphaVantageForeignExchangePullableImplTest {

    @InjectMocks
    AlphaVantageForeignExchangePullableImpl alphaVantageForeignExchangePullable;

    @Mock
    Environment environment;

    static JsonNode sampleNode;

    @Before
    public void setup() {
        environment = mock(Environment.class);
        when(environment.getProperty("alphavantage.api-key")).thenReturn("FILL_WITH_KEY");
        alphaVantageForeignExchangePullable = new AlphaVantageForeignExchangePullableImpl(environment);
    }

    @Test
    public void test01_pullCurrencyExchangeRate() {
        Optional<JsonNode> jsonNode = alphaVantageForeignExchangePullable.pullCurrencyExchangeRate(
                Currency.USD, Currency.PLN
        );
        assertTrue(jsonNode.isPresent());
        sampleNode = jsonNode.get();
        assertTrue(sampleNode.get("Realtime Currency Exchange Rate").toString().contains("1. From_Currency Code"));
        assertTrue(sampleNode.get("Realtime Currency Exchange Rate").toString().contains("3. To_Currency Code"));
    }

    @Test
    public void test02_toForeignExchangeRateInfo() {
        Optional<ForeignExchangeRateInfo> fxInfoOpt = alphaVantageForeignExchangePullable.toForeignExchangeRateInfo(sampleNode);
        assertTrue(fxInfoOpt.isPresent());
        ForeignExchangeRateInfo fxInfo = fxInfoOpt.get();
        assertEquals(fxInfo.getFromCurrency(), Currency.USD);
        assertEquals(fxInfo.getToCurrency(), Currency.PLN);
    }
}