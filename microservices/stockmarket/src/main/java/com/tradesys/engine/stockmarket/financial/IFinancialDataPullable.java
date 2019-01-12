package com.tradesys.engine.stockmarket.financial;

import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;
import com.tradesys.engine.stockmarket.utils.exceptions.MalformedPullUrlException;

import java.util.Optional;

public interface IFinancialDataPullable {

    void validateUrl(String url) throws MalformedPullUrlException;

    Optional<Object> pull(String url);

    Optional<PullInfoDataLog> toPullInfoDataLog(Long processid, Object data);

}
