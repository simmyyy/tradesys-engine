package com.tradesys.engine.stockmarket.engine;

import com.tradesys.engine.stockmarket.fxservice.ForeignExchangeDataProvider;
import com.tradesys.engine.stockmarket.kafkaservice.KafkaExchangeRatePublisher;
import com.tradesys.engine.stockmarket.utils.ForeignExchangeRateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
/**
 * Class under development
 */
public class StreamingEngine {

    private final ForeignExchangeDataPullingEngine foreignExchangeDataPullingEngine;
    private final KafkaExchangeRatePublisher kafkaExchangeRatePublisher;

    private boolean fluxData = Boolean.TRUE;
    Flux<ForeignExchangeRateInfo> rates;


    @Autowired
    public StreamingEngine(ForeignExchangeDataPullingEngine foreignExchangeDataPullingEngine,
                           KafkaExchangeRatePublisher kafkaExchangeRatePublisher) {
        this.foreignExchangeDataPullingEngine = foreignExchangeDataPullingEngine;
        this.kafkaExchangeRatePublisher = kafkaExchangeRatePublisher;
    }

    public void getForeignExchangeFlux() {
        rates = Flux
                .create(emitter -> {
                    while (true) {
                        foreignExchangeDataPullingEngine
                                .pullForeignExchangeRateInfo(ForeignExchangeDataProvider.ALPHAVANTAGE)
                                .forEach(emitter::next);
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void startFlux() {
        rates.subscribe(kafkaExchangeRatePublisher::sendExchangeRate);
    }

    public void stopFlux() {
        rates.cancelOn(Schedulers.single());
        Schedulers.shutdownNow();
    }

    public boolean isFluxData() {
        return fluxData;
    }

    public void setFluxData(boolean fluxData) {
        this.fluxData = fluxData;
    }
}
