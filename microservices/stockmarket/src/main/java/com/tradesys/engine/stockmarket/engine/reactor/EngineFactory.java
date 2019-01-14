package com.tradesys.engine.stockmarket.engine.reactor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EngineFactory {

    private final RestBatchEngineImpl restBatchEngine;
    private final RestStreamingEngineImpl restStreamingEngine;

    public IEngine getEngineImpl(PullableMetadata pullableMetadata) {
        if (pullableMetadata.getIntervalInMills() < 1) {
            return restBatchEngine;
        } else {
            return restStreamingEngine;
        }
    }

}
