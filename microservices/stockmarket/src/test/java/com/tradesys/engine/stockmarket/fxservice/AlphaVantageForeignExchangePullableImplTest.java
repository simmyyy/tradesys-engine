package com.tradesys.engine.stockmarket.fxservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradesys.engine.stockmarket.utils.Currency;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlphaVantageForeignExchangePullableImplTest {

    @InjectMocks
    AlphaVantageForeignExchangePullableImpl alphaVantageForeignExchangePullable;

    @Mock
    Environment environment;

    @Before
    public void setup() {
        environment = mock(Environment.class);
        when(environment.getProperty("alphavantage.api-key")).thenReturn("PUT_KEY_LATER");
        alphaVantageForeignExchangePullable = new AlphaVantageForeignExchangePullableImpl(environment);
    }

    @Test
    public void pullCurrencyExchangeRate() {
        Optional<JsonNode> jsonNode = alphaVantageForeignExchangePullable.pullCurrencyExchangeRate(
                Currency.USD, Currency.PLN
        );
        assertTrue(jsonNode.isPresent());
        System.out.println(jsonNode.get().get("Realtime Currency Exchange Rate").toString());
        assertTrue(jsonNode.get().get("Realtime Currency Exchange Rate").toString().contains("1. From_Currency Code"));
        assertTrue(jsonNode.get().get("Realtime Currency Exchange Rate").toString().contains("3. To_Currency Code"));
    }

//    @Test
//    public void toForeignExchangeRateInfo() {
//    }
}