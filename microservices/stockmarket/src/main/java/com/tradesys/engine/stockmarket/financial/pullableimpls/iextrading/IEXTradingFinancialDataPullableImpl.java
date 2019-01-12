package com.tradesys.engine.stockmarket.financial.pullableimpls.iextrading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradesys.engine.stockmarket.financial.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class IEXTradingFinancialDataPullableImpl implements IFinancialDataPullable {

    @Override
    public void validateUrl(String url) throws MalformedPullUrlException {
        if (!url.startsWith("https://api.iextrading.com/1.0")) {
            throw new MalformedPullUrlException("Can't execute provided url. Either malformed or insecure.");
        }
    }

    @Override
    public Optional<Object> pull(PullableMetadata metadata, String url) {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("Process ID: " + metadata.getProcessId());
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode responseBody = mapper.readTree(response.getBody());
            return Optional.of(responseBody);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while getting data from IEXTrading. " +
                    "API URL: " + url);
        }
    }
}