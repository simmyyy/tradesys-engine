package com.tradesys.engine.stockmarket.engine.reactor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@Component
public class RestStreamingEngineImpl implements IEngine<Stream<PullInfoDataLog>> {

    @Override
    public Flux<Stream<PullInfoDataLog>> createFlux(List<String> inputSource, PullableMetadata metadata, IFinancialDataPullable dataProviderPullableImpl) {
        return Flux.interval(Duration.ofMillis(metadata.getIntervalInMills()))
                .map(tick -> inputSource.stream()
                        .map(s -> {
                            Object data = dataProviderPullableImpl
                                    .pull(metadata, s)
                                    .orElseThrow(() -> new RuntimeException("No data found for execution of URL: " + s));
                            return dataProviderPullableImpl.toPullInfoDataLog(metadata, s, data);
                        }));
    }

    @Override
    public Disposable writeToDownStream(IFinancialDataWritable dataProviderWritableImpl, PullableMetadata metadata, Flux<Stream<PullInfoDataLog>> inputFlux) {
        return inputFlux.subscribe(s -> s.forEach(data -> dataProviderWritableImpl.write(metadata, data)));
    }
}
