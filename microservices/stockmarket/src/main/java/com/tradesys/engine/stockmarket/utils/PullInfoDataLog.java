package com.tradesys.engine.stockmarket.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PullInfoDataLog {

    private Long processId;
    private LocalDateTime startTime;
    private Object data;

}
