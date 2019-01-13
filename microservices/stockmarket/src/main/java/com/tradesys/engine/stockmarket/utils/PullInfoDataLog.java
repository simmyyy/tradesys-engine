package com.tradesys.engine.stockmarket.utils;


import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PullInfoDataLog {

    private DataProvider dataProvider;
    private Long processid;
    private String urlExecuted;
    private LocalDateTime startTime;
    private Object data;

}
