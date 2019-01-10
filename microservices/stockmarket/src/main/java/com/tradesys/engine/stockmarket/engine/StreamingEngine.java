package com.tradesys.engine.stockmarket.engine;

import com.tradesys.engine.stockmarket.fxservice.ForeignExchangeDataProvider;
import com.tradesys.engine.stockmarket.kafkaservice.KafkaExchangeRatePublisher;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
import com.tradesys.engine.stockmarket.utils.KafkaErrorMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
/**
 * Class under development
 */
public class StreamingEngine {

    private final ForeignExchangeDataPullingEngine foreignExchangeDataPullingEngine;
    private final KafkaExchangeRatePublisher kafkaExchangeRatePublisher;
    Flux<List<ForeignExchangeRateInfo>> rates;


    @Autowired
    public StreamingEngine(ForeignExchangeDataPullingEngine foreignExchangeDataPullingEngine,
                           KafkaExchangeRatePublisher kafkaExchangeRatePublisher) {
        this.foreignExchangeDataPullingEngine = foreignExchangeDataPullingEngine;
        this.kafkaExchangeRatePublisher = kafkaExchangeRatePublisher;
    }

    //should be parametrized
    public void getForeignExchangeFlux() {
        rates = Flux
                .interval(Duration.ofSeconds(30))
                .map(tick -> foreignExchangeDataPullingEngine
                        .pullForeignExchangeRateInfo(ForeignExchangeDataProvider.ALPHAVANTAGE)
                )
                .filter(Objects::nonNull)
                .doOnError(e -> kafkaExchangeRatePublisher.
                        sendErrorInfo(new KafkaErrorMsg(
                                e.getMessage(),
                                LocalDateTime.now())))
                .doOnComplete(() -> log.info("Finished fx flux successfully, time: " + LocalDateTime.now().toString()))
        ;
    }

    public void startFlux() {
        rates.subscribe(s -> s.forEach(kafkaExchangeRatePublisher::sendExchangeRate));
    }

    public void stopFlux() {
        rates.cancelOn(Schedulers.immediate());
    }
}
