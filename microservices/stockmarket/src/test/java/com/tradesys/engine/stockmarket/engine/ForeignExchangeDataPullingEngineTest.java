package com.tradesys.engine.stockmarket.engine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class ForeignExchangeDataPullingEngineTest {

    @Mock
    ForeignExchangeDataPullingEngine foreignExchangeDataPullingEngine;

    @Before
    public void setup() {
        initMocks(this);
    }

}