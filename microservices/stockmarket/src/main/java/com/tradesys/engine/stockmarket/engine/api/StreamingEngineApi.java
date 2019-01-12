package com.tradesys.engine.stockmarket.engine.api;


import com.tradesys.engine.stockmarket.engine.FinancialReactiveStreamingEngine;
import com.tradesys.engine.stockmarket.engine.dto.ActiveProcessStatusDTO;
import com.tradesys.engine.stockmarket.engine.dto.StreamingApiParamsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engine")
@AllArgsConstructor
public class StreamingEngineApi {

    private final FinancialReactiveStreamingEngine financialReactiveStreamingEngine;

    @PostMapping("/processes")
    public ResponseEntity<?> startProcess(@RequestBody StreamingApiParamsDTO params) {
        ActiveProcessStatusDTO status = financialReactiveStreamingEngine.startStreaming(params.getProcessId(), params.getUrls());
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @GetMapping("/processes/{processesId}")
    public String getProcessStatus(@PathVariable Long processId) {
        //dummy
        return "OK";
    }

}
