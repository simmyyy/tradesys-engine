package com.tradesys.engine.stockmarket.engine.processor;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActiveProcess {

    private Long processId;
    private Object fluxReference;
    private PullableMetadata metadata;
    private ProcessStatus status;

}
