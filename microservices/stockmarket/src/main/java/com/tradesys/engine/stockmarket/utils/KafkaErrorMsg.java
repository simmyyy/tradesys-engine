package com.tradesys.engine.stockmarket.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class KafkaErrorMsg {

    private String errorMsg;
    private LocalDateTime occurenceTime;

}
