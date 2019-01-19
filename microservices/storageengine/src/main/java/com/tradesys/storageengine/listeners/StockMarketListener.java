package com.tradesys.storageengine.listeners;

import com.tradesys.storageengine.service.KafkaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@Slf4j
@AllArgsConstructor
public class StockMarketListener {

    private final KafkaService kafkaService;

    @KafkaListener(topics = "fxrate", groupId = "foo")
    public void listenFXRate(String message) {
        kafkaService.save("fxrate", message);
    }

    @KafkaListener(topics = "stock_chart", groupId = "foo")
    public void listenStockChart(String message) {
        kafkaService.save("stock_chart", message);
    }

    @KafkaListener(topics = "earnings", groupId = "foo")
    public void listenEarnings(String message) {
        kafkaService.save("earnings", message);
    }

    @KafkaListener(topics = "ref_data", groupId = "foo")
    public void listenRefData(String message) {
        kafkaService.save("ref_data", message);
    }

    @KafkaListener(topics = "company", groupId = "foo")
    public void listenCompany(String message) {
        kafkaService.save("company", message);
    }

    @KafkaListener(topics = "financial", groupId = "foo")
    public void listenFinancial(String message) {
        kafkaService.save("financial", message);
    }

    @KafkaListener(topics = "sector_performance", groupId = "foo")
    public void listenSectorPerformance(String message) {
        kafkaService.save("sector_performance", message);
    }

    @KafkaListener(topics = "stock_price", groupId = "foo")
    public void listenStockPrice(String message) {
        kafkaService.save("stock_price", message);
    }


}
