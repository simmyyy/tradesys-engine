package com.tradesys.engine.stockmarket.financial.writable;

import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.utils.PullInfoDataLog;

public interface IFinancialDataWritable {

    void write(PullableMetadata metadata, PullInfoDataLog info);

}
