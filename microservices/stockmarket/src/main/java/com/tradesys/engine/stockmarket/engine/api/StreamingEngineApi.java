package com.tradesys.engine.stockmarket.engine.api;


import com.tradesys.engine.stockmarket.engine.FinancialReactiveStreamingEngine;
import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.engine.dto.StreamingApiParamsDTO;
import com.tradesys.engine.stockmarket.financial.pullableimpls.iextrading.IEXTradingServiceUtils;
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

    private final FinancialReactiveStreamingEngine financialReactiveStreamingEngine;
    private final IEXTradingServiceUtils iexTradingServiceUtils;

    @PostMapping("/processes")
    public ResponseEntity<?> startProcess(@RequestBody StreamingApiParamsDTO params) {
        ActiveProcessStatusDTO status = financialReactiveStreamingEngine.startStreaming(params.getProcessId(), params.getUrls());
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @DeleteMapping("/processes")
    public ResponseEntity<?> stopProcess(@RequestParam long processId) {
        ActiveProcessStatusDTO status = financialReactiveStreamingEngine.finishStreaming(processId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/iex-processes/all-symbols")
    public ResponseEntity<?> startIEXProcesses(@RequestBody StreamingApiParamsDTO params) {
        List<String> rewrittenUrls = iexTradingServiceUtils.withAllSymbols(params.getUrls().get(0));
//        log.info(rewrittenUrls.toString());
        ActiveProcessStatusDTO status = financialReactiveStreamingEngine.startStreaming(params.getProcessId(), rewrittenUrls);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @GetMapping("/processes/{processesId}")
    public ResponseEntity<?> getProcessStatus(@PathVariable("processesId") Long processId) {
        ActiveProcessStatusDTO status = new ActiveProcessStatusDTO(financialReactiveStreamingEngine.findActiveProcessById(processId));
        return new ResponseEntity<>(status, HttpStatus.FOUND);
    }

    @GetMapping("/processes")
    public ResponseEntity<?> getActiveProcesses() {
        return new ResponseEntity<>(financialReactiveStreamingEngine.getActiveProcesses(), HttpStatus.OK);
    }

}
