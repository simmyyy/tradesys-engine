package com.tradesys.engine.stockmarket.fxapi;


import com.tradesys.engine.stockmarket.engine.StreamingEngine;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-rates")
@AllArgsConstructor
//testing only
public class SystemRateInfoController {

    private final StreamingEngine streamingEngine;

    @GetMapping
    public void isss() {
        streamingEngine.getForeignExchangeFlux();
    }

    @GetMapping("/start-flux")
    public void whatever() {
        streamingEngine.startFlux();
    }

    @GetMapping("/stop-flux")
    public void whatever2() {
        streamingEngine.stopFlux();
    }

}
