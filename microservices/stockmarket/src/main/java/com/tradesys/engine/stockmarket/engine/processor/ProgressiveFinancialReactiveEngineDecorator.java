package com.tradesys.engine.stockmarket.engine.processor;

import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.engine.reactor.EngineFactory;
import com.tradesys.engine.stockmarket.financial.pullable.DataProvidersFactory;
import com.tradesys.engine.stockmarket.financial.service.MetadataService;
import com.tradesys.engine.stockmarket.financial.writable.DataProviderWritableFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProgressiveFinancialReactiveEngineDecorator extends FinancialReactiveEngine {

    private IProcessor processor;
    private int inputBatchSize = 5;
    private long delayTime = 60000;

    public ProgressiveFinancialReactiveEngineDecorator(DataProvidersFactory dataProvidersFactory, DataProviderWritableFactory dataProviderWritableFactory, MetadataService metadataService, EngineFactory engineFactory) {
        super(dataProvidersFactory, dataProviderWritableFactory, metadataService, engineFactory);
    }

    public IProcessor setProcessor(IProcessor processor) {
        this.processor = processor;
        return this;
    }

    public IProcessor setInputBatchSize(int inputBatchSize) {
        this.inputBatchSize = inputBatchSize;
        return this;
    }

    public IProcessor setDelayTime(long delayTime) {
        this.delayTime = delayTime;
        return this;
    }


    public ActiveProcessStatusDTO startProcess(final Long processId, final List<String> apiUrls) {
        log.info("Starting progressive processId: " + processId);
        for (int i = 0; i < apiUrls.size() + inputBatchSize - 1; i = i + inputBatchSize) {
            log.info("Starting progressive batch-range, min: " + i + ", max: " + (i + inputBatchSize));
            int max;
            if (i + inputBatchSize > apiUrls.size() - 1) {
                max = apiUrls.size() - 1;
            } else {
                max = i + inputBatchSize;
            }
            List<String> urls = apiUrls.subList(i, max);
            processor.startProcess(processId, urls);
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processor.finishProcess(processId);
        }
        log.info("Finished progressive processId: " + processId);
        return new ActiveProcessStatusDTO(processor.findActiveProcessById(processId));
    }


}
