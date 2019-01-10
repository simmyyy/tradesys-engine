package com.tradesys.storageengine.service;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ForeignExchangeRateInfoService {

    private final MongoTemplate mongoTemplate;

    public void save(String fxRateInfoJson) {
        try {
            mongoTemplate.createCollection("fxrate");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        mongoTemplate.insert((DBObject) JSON.parse(fxRateInfoJson), "fxrate");
    }


}
