package com.tradesys.engine.stockmarket.financial.pullableimpls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradesys.engine.stockmarket.financial.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
/**
 * Class under development
 */
public class AlphaVantageFinancialDataPullableImpl implements IFinancialDataPullable {

    private final String apiKey;

    @Autowired
    public AlphaVantageFinancialDataPullableImpl(Environment environment) {
        this.apiKey = environment.getProperty("alphavantage.api-key");
    }

    @Override
    public void validateUrl(String url) throws MalformedPullUrlException {
        if (!url.startsWith("https://www.alphavantage.co/query?function")) {
            throw new MalformedPullUrlException("Can't execute provided url. Either malformed or insecure.");
        }
    }

    @Override
    public Optional<Object> pull(String url) {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("From currency: " + url);
        log.debug("API_KEY: " + apiKey);
        try {
            String fullQueryUrl = getFullQueryUrl(url);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(fullQueryUrl, String.class);
            return Optional.of(mapper.readTree(response.getBody()));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while getting data for Foreign Exchange. " +
                    "API URL: " + url);
        }
    }

    private String getFullQueryUrl(String queryUrl) {
        return String.format(queryUrl, this.apiKey);
    }

    @Override
    public Optional<PullInfoDataLog> toPullInfoDataLog(Long processid, Object data) {
        try {
            return Optional.of(new PullInfoDataLog(processid, LocalDateTime.now(), data));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }
}
