package com.tradesys.engine.stockmarket.engine.reactor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IEngine<T> {

    Flux<T> createFlux(List<String> inputLocations, PullableMetadata metadata, IFinancialDataPullable pullable);

    Disposable writeToDownStream(IFinancialDataWritable iFinancialDataWritable, PullableMetadata pullableMetadata, Flux<T> inputFlux);

}
