package com.tradesys.engine.stockmarket.financial.service;


import com.tradesys.engine.stockmarket.financial.pullable.DataProvider;
import com.tradesys.engine.stockmarket.financial.model.PullableMetadata;
import com.tradesys.engine.stockmarket.financial.repository.PullableMetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class MetadataService {

    private final PullableMetadataRepository pullableMetadataRepository;

    public List<DataProvider> getAllDataProviders() {
        return Arrays.asList(DataProvider.values());
    }

    public List<PullableMetadata> getAllURLsByDataProvider(DataProvider dataProvider) {
        return pullableMetadataRepository.getAllByProvider(dataProvider);
    }

    public PullableMetadata getPullableMetadataById(Long id) {
        return pullableMetadataRepository.getByProcessId(id);
    }


}
