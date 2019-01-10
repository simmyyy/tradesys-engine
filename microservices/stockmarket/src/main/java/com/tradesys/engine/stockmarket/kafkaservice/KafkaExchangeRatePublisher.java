package com.tradesys.engine.stockmarket.kafkaservice;

import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
import com.tradesys.engine.stockmarket.utils.KafkaErrorMsg;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
/**
 * class under development
 */
public class KafkaExchangeRatePublisher {

    KafkaTemplate<String, ForeignExchangeRateInfo> foreignExchangeRateInfoKafkaTemplate;
    KafkaTemplate<String, KafkaErrorMsg> errorMsgKafkaTemplate;

    public void sendExchangeRate(ForeignExchangeRateInfo info) {
        foreignExchangeRateInfoKafkaTemplate.send("fxrate", info);
    }

    public void sendErrorInfo(KafkaErrorMsg msg) {
        errorMsgKafkaTemplate.send("streaming-errors", msg);
    }

}
