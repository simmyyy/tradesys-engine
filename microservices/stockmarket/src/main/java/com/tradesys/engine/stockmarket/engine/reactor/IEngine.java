package com.tradesys.engine.stockmarket.engine.reactor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

public interface IEngine<T> {

    Flux<T> createFlux(List<String> inputLocations, PullableMetadata metadata, IFinancialDataPullable pullable);

    Disposable writeToDownStream(IFinancialDataWritable iFinancialDataWritable, PullableMetadata pullableMetadata, Flux<T> inputFlux);

}
