package com.tradesys.engine.stockmarket.engine;

import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.financial.DataProvidersFactory;
import com.tradesys.engine.stockmarket.financial.IFinancialDataPullable;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.service.MetadataService;
import com.tradesys.engine.stockmarket.kafkaservice.KafkaExchangeRatePublisher;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessAlreadyDefinedException;
import com.tradesys.engine.stockmarket.utils.exceptions.ProcessNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
/**
 * Class under development
 */
public class FinancialReactiveStreamingEngine {


    private final DataProvidersFactory dataProvidersFactory;
    private final KafkaExchangeRatePublisher kafkaExchangeRatePublisher;
    private final MetadataService metadataService;

    private static List<ActiveProcess> activeProcesses = Collections.synchronizedList(new ArrayList<>());


    @Autowired
    public FinancialReactiveStreamingEngine(DataProvidersFactory dataProvidersFactory,
                                            KafkaExchangeRatePublisher kafkaExchangeRatePublisher,
                                            MetadataService metadataService) {
        this.dataProvidersFactory = dataProvidersFactory;
        this.kafkaExchangeRatePublisher = kafkaExchangeRatePublisher;
        this.metadataService = metadataService;
    }


    public ActiveProcessStatusDTO startStreaming(Long processId, List<String> apiUrls) {
        PullableMetadata metadata = metadataService.getPullableMetadataById(processId);
        createFluxProcess(metadata)
                .getFluxReference()
                .map(tick -> {
                    IFinancialDataPullable pullable = dataProvidersFactory.getFinancialDataPullableImpl(metadata.getDataProvider());
                    return apiUrls.parallelStream()
                            .map(pullable::pull)
                            .map(obj -> pullable.toPullInfoDataLog(metadata.getProcessId(), obj))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());

                })
                .filter(Objects::nonNull)
                .subscribe(s -> s.forEach(kafkaExchangeRatePublisher::sendExchangeRate));
        ActiveProcess activeProcess = findActiveProcessById(processId);
        activeProcess.setStatus(ProcessStatus.ONGOING);
        return new ActiveProcessStatusDTO(activeProcess);

    }

    private ActiveProcess createFluxProcess(PullableMetadata pullableMetadata) {
        Optional<ActiveProcess> activeProcess = activeProcesses.stream().filter(s -> s.getProcessId().equals(pullableMetadata.getProcessId())).findFirst();
        if (!activeProcess.isPresent()) {
            ActiveProcess processStatus = new ActiveProcess(pullableMetadata.getProcessId(), Flux.interval(Duration.ofMillis(pullableMetadata.getIntervalInMills())), pullableMetadata, ProcessStatus.STARTED);
            activeProcesses.add(processStatus);
            return processStatus;
        } else {
            ActiveProcess processStatus = activeProcess.get();
            if (processStatus.getStatus().equals(ProcessStatus.FINISHED)) {
                processStatus.setStatus(ProcessStatus.STARTED);
                processStatus.setFluxReference(Flux.interval(Duration.ofMillis(pullableMetadata.getIntervalInMills())));
                return processStatus;
            } else {
                throw new ProcessAlreadyDefinedException("Process is already defined in the system as running or failed. Please check metadata");
            }
        }

    }

    public ActiveProcess findActiveProcessById(Long id) {
        Optional<ActiveProcess> activeProcess = activeProcesses.stream().filter(s -> s.getProcessId().equals(id)).findFirst();
        if (activeProcess.isPresent()) {
            return activeProcess.get();
        } else {
            throw new ProcessNotFoundException("Can't find process with id: " + id);
        }
    }

    //should be parametrized
//    public void getForeignExchangeFlux() {
//        rates = Flux
//                .interval(Duration.ofSeconds(30))
//                .map(tick -> foreignExchangeDataPullingEngine
//                        .pullForeignExchangeRateInfo(DataProvider.ALPHAVANTAGE)
//                )
//                .filter(Objects::nonNull)
//                .doOnError(e -> kafkaExchangeRatePublisher.
//                        sendErrorInfo(new KafkaErrorMsg(
//                                e.getMessage(),
//                                LocalDateTime.now())))
//                .doOnComplete(() -> log.info("Finished fx flux successfully, time: " + LocalDateTime.now().toString()))
//        ;
//    }


    public void startFlux() {

    }

    //
    public void stopFlux() {
//        rates.cancelOn(Schedulers.immediate());
    }
}
