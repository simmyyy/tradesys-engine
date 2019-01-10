package com.tradesys.storageengine.listeners;

import com.tradesys.storageengine.service.ForeignExchangeRateInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@Slf4j
@AllArgsConstructor
public class ForeignExchangeListener {


    private final ForeignExchangeRateInfoService foreignExchangeRateInfoService;

    @KafkaListener(topics = "fxrate", groupId = "foo")
    public void listen(String message) {
        log.info(message);
        foreignExchangeRateInfoService.save(message);
    }

}
