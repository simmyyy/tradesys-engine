package com.tradesys.engine.stockmarket.utils;


import lombok.Getter;

@Getter
public enum Currency {

    USD("United States Dollar"), PLN("Polish Zloty"), BTC("Bitcoin");

    private String description;

    Currency(String description) {
        this.description = description;
    }

}
