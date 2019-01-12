package com.tradesys.engine.stockmarket.financial.model;

import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import com.tradesys.engine.stockmarket.financial.writable.DownstreamSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PullableMetadata {

    private Long processId;
    private DataProvider dataProvider;
    private Long intervalInMills;
    private String apiURL;
    private String description;
    private String sample;
    private String kafkaCollection;
    private int kafkaPartition;
    private DownstreamSystem downstreamSystem;

}
