package com.tradesys.engine.stockmarket.engine.processor;

import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IProcessor {

    ActiveProcessStatusDTO startProcess(final Long processId, final List<String> inputLocations);

    ActiveProcess insertNewProcess(PullableMetadata pullableMetadata, Flux fluxReference);

    ActiveProcessStatusDTO finishProcess(Long processId);

    ActiveProcess findActiveProcessById(Long id);

    List<ActiveProcess> getActiveProcesses();

}
