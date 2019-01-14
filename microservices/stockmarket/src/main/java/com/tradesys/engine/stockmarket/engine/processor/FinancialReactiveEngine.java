package com.tradesys.engine.stockmarket.engine.processor;

import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.engine.reactor.EngineFactory;
import com.tradesys.engine.stockmarket.engine.reactor.IEngine;
import com.tradesys.engine.stockmarket.financial.pullable.DataProvidersFactory;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.service.MetadataService;
import com.tradesys.engine.stockmarket.financial.writable.DataProviderWritableFactory;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessAlreadyDefinedException;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
@Slf4j
/**
 * Class under development
 */
public class FinancialReactiveEngine implements IProcessor {

    private final DataProvidersFactory dataProvidersFactory;
    private final MetadataService metadataService;
    private final DataProviderWritableFactory dataProviderWritableFactory;
    private final EngineFactory engineFactory;
    private static List<ActiveProcess> activeProcesses = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    public FinancialReactiveEngine(DataProvidersFactory dataProvidersFactory,
                                   DataProviderWritableFactory dataProviderWritableFactory,
                                   MetadataService metadataService,
                                   EngineFactory engineFactory) {
        this.dataProvidersFactory = dataProvidersFactory;
        this.dataProviderWritableFactory = dataProviderWritableFactory;
        this.metadataService = metadataService;
        this.engineFactory = engineFactory;
    }

    public ActiveProcessStatusDTO startProcess(final Long processId, final List<String> apiUrls) {
        log.info("Starting process with processID: " + processId);
        final PullableMetadata metadata = metadataService.getPullableMetadataById(processId);
        final IFinancialDataPullable dataProviderPullableImpl = dataProvidersFactory.getFinancialDataPullableImpl(metadata.getDataProvider());
        final IFinancialDataWritable dataProviderWritableImpl = dataProviderWritableFactory.getDataProviderWritableImpl(metadata.getDataProvider(), metadata.getDownstreamSystem());
        final IEngine engine = engineFactory.getEngineImpl(metadata);
        Flux inputFlux = engine.createFlux(apiUrls, metadata, dataProviderPullableImpl);
        ActiveProcess activeProcess = insertNewProcess(metadata, inputFlux);
        Disposable ref = engine.writeToDownStream(dataProviderWritableImpl, metadata, inputFlux);
        activeProcess.setStatus(ProcessStatus.ONGOING);
        activeProcess.setFluxReference(ref);
        log.info("Scheduler process for process id: " + processId);
        return new ActiveProcessStatusDTO(activeProcess);
    }

    public ActiveProcess insertNewProcess(PullableMetadata pullableMetadata, Flux fluxReference) {
        Optional<ActiveProcess> activeProcess = activeProcesses
                .stream()
                .filter(s -> s.getProcessId().equals(pullableMetadata.getProcessId()))
                .findFirst();
        if (!activeProcess.isPresent()) {
            ActiveProcess processStatus = new ActiveProcess(
                    pullableMetadata.getProcessId(),
                    fluxReference,
                    pullableMetadata,
                    ProcessStatus.STARTED);
            activeProcesses.add(processStatus);
            return processStatus;
        } else {
            ActiveProcess processStatus = activeProcess.get();
            if (processStatus.getStatus().equals(ProcessStatus.FINISHED)) {
                processStatus.setStatus(ProcessStatus.STARTED);
                processStatus.setFluxReference(fluxReference);
                return processStatus;
            } else {
                throw new ProcessAlreadyDefinedException("Process is already defined in the system as running or failed. Please check metadata");
            }
        }
    }

    public ActiveProcessStatusDTO finishProcess(Long processId) {
        ActiveProcess activeProcess = findActiveProcessById(processId);
        if (!activeProcess.getStatus().equals(ProcessStatus.FINISHED)) {
            ((Disposable) activeProcess.getFluxReference()).dispose();
            activeProcess.setStatus(ProcessStatus.FINISHED);
        }
        log.info("Finished process with processId: " + processId);
        return new ActiveProcessStatusDTO(activeProcess);
    }

    public ActiveProcess findActiveProcessById(Long id) {
        Optional<ActiveProcess> activeProcess = activeProcesses
                .stream()
                .filter(s -> s.getProcessId().equals(id))
                .findFirst();
        if (activeProcess.isPresent()) {
            return activeProcess.get();
        } else {
            throw new ProcessNotFoundException("Can't find process with id: " + id);
        }
    }

    public List<ActiveProcess> getActiveProcesses() {
        return activeProcesses;
    }
}