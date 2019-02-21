package com.limbo.actual.mocktrading.service;

import com.limbo.actual.mocktrading.constant.CommonConstant;
import com.limbo.actual.mocktrading.model.Commodity;
import com.limbo.actual.mocktrading.model.TradingDetail;
import com.limbo.actual.mocktrading.repository.CommodityRepository;
import com.limbo.actual.mocktrading.repository.PersonRepository;
import com.limbo.actual.mocktrading.repository.StoreRepository;
import com.limbo.actual.mocktrading.util.JackJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradingService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * 处理交易付款
     *
     * @param tradingJson 交易详情
     */
    @KafkaListener(
            topics = "trading_payer",
//            group = "handlePay",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handlePay(String tradingJson) {
        TradingDetail trading = JackJsonUtil.toBean(tradingJson, TradingDetail.class);
        //System.out.println("person : [" + trading.getPayer().getName() + "] pay commodity : [" + trading.getCommodity().getName() + "] price : [" + trading.getCommodity().getPrice() + "]");
        int result = personRepository.pay(trading.getPayer().getId(), trading.getCommodity().getPrice());
        // 付款成功后进行入账操作
        if (result == CommonConstant.DB_SUCCESS) {
            trading.setPayStatus(CommonConstant.SUCCESS);
            kafkaTemplate.send("trading_payee", JackJsonUtil.toJson(trading));
        }
        // 付款失败，进入失败统计
        else {
            trading.setPayStatus(CommonConstant.FAIL);
            kafkaTemplate.send("trading_fail", JackJsonUtil.toJson(trading));
        }

    }

    /**
     * 处理交易收款:当前情况不考虑收款的异常情景，以收款作为交易的结束
     *
     * @param tradingJson 交易详情
     */
    @KafkaListener(
            topics = "trading_payee",
//            group = "handleEarn",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleEarn(String tradingJson) {
        TradingDetail trading = JackJsonUtil.toBean(tradingJson, TradingDetail.class);
        //System.out.println("person : [" + trading.getPayee().getName() + "] earn commodity : [" + trading.getCommodity().getName() + "] price : [" + trading.getCommodity().getPrice() + "]");
        int result = personRepository.earn(trading.getPayee().getId(), trading.getCommodity().getPrice());
    }
}
