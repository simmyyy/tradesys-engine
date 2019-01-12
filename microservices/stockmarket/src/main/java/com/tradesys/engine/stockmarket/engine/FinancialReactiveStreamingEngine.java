package com.tradesys.engine.stockmarket.engine;

import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.financial.pullable.DataProvidersFactory;
import com.tradesys.engine.stockmarket.financial.pullable.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.service.MetadataService;
import com.tradesys.engine.stockmarket.financial.writable.DataProviderWritableFactory;
import com.tradesys.engine.stockmarket.financial.writable.IFinancialDataWritable;
import com.tradesys.engine.stockmarket.kafkaservice.KafkaExchangeRatePublisher;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessAlreadyDefinedException;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
/**
 * Class under development
 */
public class FinancialReactiveStreamingEngine {


    private final DataProvidersFactory dataProvidersFactory;
    private final MetadataService metadataService;
    private final DataProviderWritableFactory dataProviderWritableFactory;
    private static List<ActiveProcess> activeProcesses = Collections.synchronizedList(new ArrayList<>());


    @Autowired
    public FinancialReactiveStreamingEngine(DataProvidersFactory dataProvidersFactory,
                                            DataProviderWritableFactory dataProviderWritableFactory,
                                            MetadataService metadataService) {
        this.dataProvidersFactory = dataProvidersFactory;
        this.dataProviderWritableFactory = dataProviderWritableFactory;
        this.metadataService = metadataService;
    }


    public ActiveProcessStatusDTO startStreaming(Long processId, List<String> apiUrls) {
        PullableMetadata metadata = metadataService.getPullableMetadataById(processId);
        IFinancialDataPullable dataProviderPullableImpl = dataProvidersFactory.getFinancialDataPullableImpl(metadata.getDataProvider());
        IFinancialDataWritable dataProviderWritableImpl = dataProviderWritableFactory.getDataProviderWritableImpl(metadata.getDataProvider(), metadata.getDownstreamSystem());
        Disposable ref = createFluxProcess(metadata)
                .map(tick -> apiUrls.parallelStream()
                        .map(s -> dataProviderPullableImpl.pull(metadata, s))
                        .map(obj -> dataProviderPullableImpl.toPullInfoDataLog(metadata.getProcessId(), obj)))
                .filter(Objects::nonNull)
                .subscribe(s -> s.forEach(data -> dataProviderWritableImpl.write(metadata, data)));
        ActiveProcess activeProcess = findActiveProcessById(processId);
        activeProcess.setStatus(ProcessStatus.ONGOING);
        activeProcess.setFluxReference(ref);
        return new ActiveProcessStatusDTO(activeProcess);
    }

    private Flux<?> createFluxProcess(PullableMetadata pullableMetadata) {
        Optional<ActiveProcess> activeProcess = activeProcesses.stream().filter(s -> s.getProcessId().equals(pullableMetadata.getProcessId())).findFirst();
        if (!activeProcess.isPresent()) {
            ActiveProcess processStatus = new ActiveProcess(pullableMetadata.getProcessId(), Flux.interval(Duration.ofMillis(pullableMetadata.getIntervalInMills())), pullableMetadata, ProcessStatus.STARTED);
            activeProcesses.add(processStatus);
            return (Flux<?>) processStatus.getFluxReference();
        } else {
            ActiveProcess processStatus = activeProcess.get();
            if (processStatus.getStatus().equals(ProcessStatus.FINISHED)) {
                processStatus.setStatus(ProcessStatus.STARTED);
                processStatus.setFluxReference(Flux.interval(Duration.ofMillis(pullableMetadata.getIntervalInMills())));
                return (Flux<?>) processStatus.getFluxReference();
            } else {
                throw new ProcessAlreadyDefinedException("Process is already defined in the system as running or failed. Please check metadata");
            }
        }
    }

    public ActiveProcessStatusDTO finishStreaming(Long processId) {
        ActiveProcess activeProcess = activeProcesses
                .parallelStream()
                .filter(s -> s.getProcessId().equals(processId))
                .findFirst()
                .orElseThrow(() -> new ProcessNotFoundException("Can't find process with id: " + processId));
        if (!activeProcess.getStatus().equals(ProcessStatus.FINISHED) || activeProcess.getStatus().equals(ProcessStatus.ERROR)) {
            ((Disposable) activeProcess.getFluxReference()).dispose();
            activeProcess.setStatus(ProcessStatus.FINISHED);
        }
        return new ActiveProcessStatusDTO(activeProcess);
    }

    public ActiveProcess findActiveProcessById(Long id) {
        Optional<ActiveProcess> activeProcess = activeProcesses.stream().filter(s -> s.getProcessId().equals(id)).findFirst();
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
