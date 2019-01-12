package com.tradesys.engine.stockmarket.financial.dpimpls.alphavantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
/**
 * Class under development
 */
public class AlphaVantageFinancialDataProviderImpl implements IFinancialDataPullable, IFinancialDataWritable {

    private final String apiKey;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AlphaVantageFinancialDataProviderImpl(Environment environment, KafkaTemplate<String, String> kafkaTemplate) {
        this.apiKey = environment.getProperty("alphavantage.api-key");
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void validateUrl(String url) throws MalformedPullUrlException {
        if (!url.startsWith("https://www.alphavantage.co/query?function")) {
            throw new MalformedPullUrlException("Can't execute provided url. Either malformed or insecure.");
        }
    }

    @Override
    public Optional<Object> pull(PullableMetadata metadata, String url) {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("Process ID: " + metadata.getProcessId());
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
    public void write(PullableMetadata metadata, PullInfoDataLog info) {
        kafkaTemplate.send(metadata.getKafkaCollection(), new Gson().toJson(info));
    }
}
