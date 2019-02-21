package com.limbo.actual.mocktrading.util;

public class TradingNumUtil {

    private static TradingNumUtil instance;

    public static TradingNumUtil getInstance(){
        if (instance == null){
            instance = new TradingNumUtil();
        }
        return instance;
    }

    private int num = 0;

    public int nextNum(){
        return num++;
    }
}
