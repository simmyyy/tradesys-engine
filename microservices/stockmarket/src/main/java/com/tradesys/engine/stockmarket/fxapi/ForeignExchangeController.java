package com.tradesys.engine.stockmarket.fxapi;


import com.tradesys.engine.stockmarket.engine.FinancialReactiveStreamingEngine;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fx-rates")
@AllArgsConstructor
//testing only
public class ForeignExchangeController {

    private final FinancialReactiveStreamingEngine financialReactiveStreamingEngine;


    @PostMapping("/startOneFXSTream")
    public ResponseEntity<?> startFXStream(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        //@TODO fill later
        return null;
    }

    @PostMapping("/stopOneFXStream")
    public ResponseEntity<?> stopFXStream(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        //@TODO fill later
        return null;
    }

    @PostMapping("/startFullFXStream")
    public ResponseEntity<?> startFXFullStream() {
        //@TODO fill later
        return null;
    }

    @PostMapping("/stopFullFXStream")
    public ResponseEntity<?> stopFXFullStream() {
        //@TODO fill later
        return null;
    }



    @GetMapping("/start-flux")
    public void whatever() {
        financialReactiveStreamingEngine.startFlux();
    }

    @GetMapping("/stop-flux")
    public void whatever2() {
        financialReactiveStreamingEngine.stopFlux();
    }

}
