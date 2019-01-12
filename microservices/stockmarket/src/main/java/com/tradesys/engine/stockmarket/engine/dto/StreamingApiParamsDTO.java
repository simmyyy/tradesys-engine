package com.tradesys.engine.stockmarket.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StreamingApiParamsDTO {

    private long processId;
    private List<String> urls;

}
