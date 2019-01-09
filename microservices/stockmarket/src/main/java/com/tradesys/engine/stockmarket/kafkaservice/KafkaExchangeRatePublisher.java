package com.tradesys.engine.stockmarket.kafkaservice;

import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
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

    public void sendExchangeRate(ForeignExchangeRateInfo info) {
        kafkaTemplate.send("fxrate", info.toString());
    }

}
