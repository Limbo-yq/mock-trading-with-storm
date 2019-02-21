package com.limbo.actual.mocktrading.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradingDetail {

    /**
     * 交易次数
     */
    private int payNumber;

    /**
     * 交易状态；0-fail，1-success
     */
    private int payStatus;

    /**
     * 付款方
     */
    private Person payer;

    /**
     * 收款方
     */
    private Person payee;

    /**
     * 交易品
     */
    private Commodity commodity;

    /**
     * 光顾的商店
     */
    private Store store;

}
