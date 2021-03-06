package com.tradesys.engine.stockmarket.engine.processor;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ProcessorFactory {

    private final FinancialReactiveEngine financialReactiveEngine;
    private final ProgressiveFinancialReactiveEngineDecorator progressiveFinancialReactiveEngineDecorator;

    public IProcessor getProcessor(ProcessorType processorType) {
        if (processorType.equals(ProcessorType.FINANCIAL_REACTIVE)) {
            return financialReactiveEngine;
        } else if (processorType.equals(ProcessorType.FINANCIAL_PROGRESSIVE_REACTIVE)) {
            return progressiveFinancialReactiveEngineDecorator.setProcessor(financialReactiveEngine);
        } else {
            throw new RuntimeException("Can't find processortype: " + processorType.toString());
        }
    }

}
