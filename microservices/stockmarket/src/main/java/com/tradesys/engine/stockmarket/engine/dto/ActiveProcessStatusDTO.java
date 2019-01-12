package com.tradesys.engine.stockmarket.engine.dto;

import com.tradesys.engine.stockmarket.engine.ActiveProcess;
import com.tradesys.engine.stockmarket.engine.ProcessStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveProcessStatusDTO {

    private long processId;
    private ProcessStatus processStatus;

    public ActiveProcessStatusDTO(ActiveProcess activeProcess) {
        this.processId = activeProcess.getProcessId();
        this.processStatus = activeProcess.getStatus();
    }

}
