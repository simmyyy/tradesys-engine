package com.tradesys.engine.stockmarket.fxservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradesys.engine.stockmarket.utils.Currency;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class AlphaVantageForeignExchangePullableImpl implements IForeignExchangePullable<JsonNode> {

    private final static String QUERY_URL = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s";
    private final String apiKey;

    @Autowired
    public AlphaVantageForeignExchangePullableImpl(Environment environment) {
        this.apiKey = environment.getProperty("alphavantage.api-key");
    }

    @Override
    public Optional<JsonNode> pullCurrencyExchangeRate(final Currency fromCurrency, final Currency toCurrency) {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("From currency: " + fromCurrency.toString());
        log.debug("To Currency: " + toCurrency.toString());
        log.debug("API_KEY: " + apiKey);
        try {
            String fullQueryUrl = getFullQueryUrl(fromCurrency, toCurrency);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(fullQueryUrl, String.class);
            return Optional.of(mapper.readTree(response.getBody()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private String getFullQueryUrl(Currency fromCurrency, Currency toCurrency) {
        return String.format(QUERY_URL, fromCurrency.toString(), toCurrency.toString(), this.apiKey);
    }

    @Override
    //@TODO what if NULL?
    public Optional<ForeignExchangeRateInfo> toForeignExchangeRateInfo(JsonNode foreignExchangeRate) {
        try {
            Currency fromCurrency = Currency.valueOf(foreignExchangeRate.findValue("1. From_Currency Code").textValue());
            Currency toCurrency = Currency.valueOf(foreignExchangeRate.findValue("3. To_Currency Code").textValue());
            BigDecimal exchangeRate = new BigDecimal(foreignExchangeRate.findValue("5. Exchange Rate").textValue());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(foreignExchangeRate.findValue("6. Last Refreshed").textValue(), dateTimeFormatter);
            ZoneId zoneId = ZoneId.of(foreignExchangeRate.findValue("7. Time Zone").textValue());
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
            return Optional.of(new ForeignExchangeRateInfo(fromCurrency, toCurrency, exchangeRate, zonedDateTime));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }
}
