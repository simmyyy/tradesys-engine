package com.tradesys.engine.stockmarket.financial.dpimpls.coinapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class COINApiForeignExchangeDataProviderImpl implements IFinancialDataPullable, IFinancialDataWritable {

    private final String apiKey;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public COINApiForeignExchangeDataProviderImpl(Environment environment, KafkaTemplate<String, String> kafkaTemplate) {
        this.apiKey = environment.getProperty("coinapi.api-key");
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void validateUrl(String url) throws MalformedPullUrlException {
        if (!url.startsWith("https://rest.coinapi.io/v1/")) {
            throw new MalformedPullUrlException("Can't execute provided url. Either malformed or insecure.");
        }
    }

    @Override
    public Optional<Object> pull(PullableMetadata metadata, String url) {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("Process ID: " + metadata.getProcessId());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-CoinAPI-Key", apiKey);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode responseBody = mapper.readTree(response.getBody());
            return Optional.of(responseBody);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while getting data from IEXTrading. " +
                    "API URL: " + url);
        }
    }


    @Override
    public void write(PullableMetadata metadata, PullInfoDataLog info) {
        kafkaTemplate.send(metadata.getKafkaCollection(), new Gson().toJson(info));
    }
}
