package com.tradesys.engine.stockmarket.kafkaservice;

import com.google.gson.Gson;
import com.tradesys.engine.stockmarket.utils.KafkaErrorMsg;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
/**
 * class under development
 */
public class KafkaExchangeRatePublisher {

    KafkaTemplate<String, String> kafkaTemplate;

    public void sendExchangeRate(PullInfoDataLog info) {
        kafkaTemplate.send("fxrate", new Gson().toJson(info));
    }

}
