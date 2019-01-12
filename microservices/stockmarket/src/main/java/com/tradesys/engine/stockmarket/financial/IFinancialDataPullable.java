package com.tradesys.engine.stockmarket.financial;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IFinancialDataPullable {

    void validateUrl(String url) throws MalformedPullUrlException;

    Optional<Object> pull(PullableMetadata pullableMetadata, String url);

    default PullInfoDataLog toPullInfoDataLog(Long processid, Object data) {
        return new PullInfoDataLog(processid, LocalDateTime.now(), data);
    }

}
