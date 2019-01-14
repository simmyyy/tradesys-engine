package com.tradesys.engine.stockmarket.financial.dpimpls.iextrading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class IEXTradingFinancialDataProviderImpl implements IFinancialDataPullable, IFinancialDataWritable {

    private final KafkaTemplate<String, String> kafkaTemplate;

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

    @Override
    public void write(PullableMetadata metadata, PullInfoDataLog info) {
        kafkaTemplate.send(metadata.getKafkaCollection(), new Gson().toJson(info));
//        kafkaTemplate.send(metadata.getKafkaCollection(), new Gson().toJson(info));
    }
}