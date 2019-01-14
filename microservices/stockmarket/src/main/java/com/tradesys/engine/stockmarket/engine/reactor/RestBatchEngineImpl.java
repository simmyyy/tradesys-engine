package com.tradesys.engine.stockmarket.engine.reactor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class RestBatchEngineImpl implements IEngine<PullInfoDataLog> {

    @Override
    public Flux<PullInfoDataLog> createFlux(List<String> inputSource, PullableMetadata metadata, IFinancialDataPullable dataProviderPullableImpl) {
        return Flux.fromStream(inputSource.stream().map(s -> {
            Object data = dataProviderPullableImpl
                    .pull(metadata, s)
                    .orElseThrow(() -> new RuntimeException("No data found for execution of URL: " + s));
            return dataProviderPullableImpl.toPullInfoDataLog(metadata, s, data);
        }));
    }

    @Override
    public Disposable writeToDownStream(IFinancialDataWritable dataProviderWritableImpl, PullableMetadata metadata, Flux<PullInfoDataLog> inputFlux) {
        return inputFlux.subscribe(data -> dataProviderWritableImpl.write(metadata, data));
    }


}
