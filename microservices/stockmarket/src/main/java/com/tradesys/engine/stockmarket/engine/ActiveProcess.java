package com.tradesys.engine.stockmarket.engine;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Getter
@Setter
@AllArgsConstructor
public class ActiveProcess {

    private Long processId;
    private Object fluxReference;
    private PullableMetadata metadata;
    private ProcessStatus status;

}
