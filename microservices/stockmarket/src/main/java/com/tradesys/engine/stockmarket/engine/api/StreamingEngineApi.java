package com.tradesys.engine.stockmarket.engine.api;


import com.tradesys.engine.stockmarket.engine.processor.*;
import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.engine.dto.StreamingApiParamsDTO;
import com.tradesys.engine.stockmarket.financial.dpimpls.alphavantage.AlphaVantageServiceUtils;
import com.tradesys.engine.stockmarket.financial.dpimpls.iextrading.IEXTradingServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engine")
@AllArgsConstructor
@Slf4j
public class StreamingEngineApi {

    private final IEXTradingServiceUtils iexTradingServiceUtils;
    private final ProcessorFactory processorFactory;
    private final AlphaVantageServiceUtils alphaVantageServiceUtils;

    @PostMapping("/processes")
    public ResponseEntity<?> startProcess(@RequestBody StreamingApiParamsDTO params) {
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_REACTIVE).startProcess(params.getProcessId(), params.getUrls());
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @DeleteMapping("/processes")
    public ResponseEntity<?> stopProcess(@RequestParam long processId) {
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_REACTIVE).finishProcess(processId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/alpha-processes/all-symbols")
    public ResponseEntity<?> startAlphantageProcesses(@RequestBody StreamingApiParamsDTO params) {
        List<String> rewrittenUrls = iexTradingServiceUtils.withAllSymbols(params.getUrls().get(0));
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_PROGRESSIVE_REACTIVE).startProcess(params.getProcessId(), rewrittenUrls);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PostMapping("/alpha-processes/all-currencies")
    public ResponseEntity<?> startAlphavantageProcessesForAllCurrencies(@RequestBody StreamingApiParamsDTO params) {
        List<String> rewrittenUrls = alphaVantageServiceUtils.withAllPhysicalCurrencies(params.getUrls().get(0));
        log.info(rewrittenUrls.toString());
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_PROGRESSIVE_REACTIVE).startProcess(params.getProcessId(), rewrittenUrls);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PostMapping("/alpha-processes/all-dig-currencies")
    public ResponseEntity<?> startAlpavantageProcessesForAllDigitalCurrencies(@RequestBody StreamingApiParamsDTO params) {
        List<String> rewrittenUrls = alphaVantageServiceUtils.withAllDigiralCurrencies(params.getUrls().get(0));
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_PROGRESSIVE_REACTIVE).startProcess(params.getProcessId(), rewrittenUrls);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PostMapping("/iex-processes/all-symbols")
    public ResponseEntity<?> startIEXProcesses(@RequestBody StreamingApiParamsDTO params) {
        List<String> rewrittenUrls = iexTradingServiceUtils.withAllSymbols(params.getUrls().get(0));
        ActiveProcessStatusDTO status = processorFactory.getProcessor(ProcessorType.FINANCIAL_REACTIVE).startProcess(params.getProcessId(), rewrittenUrls);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @GetMapping("/processes/{processesId}")
    public ResponseEntity<?> getProcessStatus(@PathVariable("processesId") Long processId) {
        ActiveProcessStatusDTO status = new ActiveProcessStatusDTO(processorFactory.getProcessor(ProcessorType.FINANCIAL_REACTIVE).findActiveProcessById(processId));
        return new ResponseEntity<>(status, HttpStatus.FOUND);
    }

    @GetMapping("/processes")
    public ResponseEntity<?> getActiveProcesses() {
        return new ResponseEntity<>(processorFactory.getProcessor(ProcessorType.FINANCIAL_REACTIVE).getActiveProcesses(), HttpStatus.OK);
    }

}
