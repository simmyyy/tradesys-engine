package com.tradesys.engine.stockmarket.utils.exceptions;

public class ProcessAlreadyDefinedException extends RuntimeException {

    public ProcessAlreadyDefinedException(String msg){
        super(msg);
    }

}
