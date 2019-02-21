package com.limbo.actual.mocktrading.controller;

import com.limbo.actual.mocktrading.model.Commodity;
import com.limbo.actual.mocktrading.model.Person;
import com.limbo.actual.mocktrading.model.Store;
import com.limbo.actual.mocktrading.model.TradingDetail;
import com.limbo.actual.mocktrading.repository.CommodityRepository;
import com.limbo.actual.mocktrading.repository.PersonRepository;
import com.limbo.actual.mocktrading.repository.StoreRepository;
import com.limbo.actual.mocktrading.util.JackJsonUtil;
import com.limbo.actual.mocktrading.util.TradingNumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/main")
public class MainController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CommodityRepository commodityRepository;

    @GetMapping("/testKafka")
    public String test(){
        kafkaTemplate.send("mytopic","test");
        return "success";
    }
    @GetMapping("/trading/{num}")
    public String startTrading(@PathVariable Integer num){
        List<Person> people = personRepository.findAll();
        List<Commodity> commodities = commodityRepository.findAll();
        List<Store> stores = storeRepository.findAll();
        Map<Long,Store> storeMap = new HashMap<>();

        for (Store store:stores){
            storeMap.put(store.getId(),store);
        }

        for (int j = 0; j < num; j++) {
            // 每次每人随机发起一次交易，以交易次数为x轴
            for (Person payer : people) {
                Person payee = people.get(new Random().nextInt(people.size()));
                Commodity commodity = commodities.get(new Random().nextInt(commodities.size()));

                TradingDetail trading = new TradingDetail();
                trading.setPayNumber(TradingNumUtil.getInstance().nextNum());
                trading.setPayer(payer);
                trading.setPayee(payee);
                trading.setCommodity(commodity);
                trading.setStore(storeMap.get(commodity.getStoreId()));

                kafkaTemplate.send("trading_payer", JackJsonUtil.toJson(trading));
            }
        }
        return "已提交所有交易";
    }
}