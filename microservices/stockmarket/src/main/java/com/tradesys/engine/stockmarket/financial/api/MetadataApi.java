package com.tradesys.engine.stockmarket.financial.api;

import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.dpimpls.iextrading.IEXTradingServiceUtils;
import com.tradesys.engine.stockmarket.financial.service.MetadataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metadata")
@AllArgsConstructor
public class MetadataApi {

    private final MetadataService metadataService;
    private final IEXTradingServiceUtils iexTradingServiceUtils;

    @GetMapping("/data-providers")
    public List<DataProvider> getDataProviders() {
        return metadataService.getAllDataProviders();
    }

    @GetMapping("/data-providers/{name}/processes")
    public List<PullableMetadata> getMetadataByDataProvider(@PathVariable("name") String dataProviderName) {
        return metadataService.getAllURLsByDataProvider(DataProvider.valueOf(dataProviderName));
    }

    @GetMapping("/processes/{processid}")
    public PullableMetadata getMetadataByProcessId(@PathVariable("processid") Long processId) {
        return metadataService.getPullableMetadataById(processId);
    }

    @GetMapping("/iex-symbols")
    public List<String> getIEXMetadataSymbols() {
        return iexTradingServiceUtils.getAllSymbols();
    }

}
