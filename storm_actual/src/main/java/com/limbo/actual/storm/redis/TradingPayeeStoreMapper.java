package com.limbo.actual.storm.redis;

import com.limbo.actual.mocktrading.model.Commodity;
import com.limbo.actual.mocktrading.model.Person;
import com.limbo.actual.mocktrading.util.JackJsonUtil;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.tuple.ITuple;
import org.json.simple.JSONObject;

public class TradingPayeeStoreMapper implements RedisStoreMapper {

    private RedisDataTypeDescription description;

    public TradingPayeeStoreMapper(){
        this.description = new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.LIST);
    }

    @Override
    public RedisDataTypeDescription getDataTypeDescription() {
        return this.description;
    }

    @Override
    public String getKeyFromTuple(ITuple iTuple) {
        Person payer = JackJsonUtil.toBean(iTuple.getStringByField("payee_json"),Person.class);
        return payer.getName()+"_payee";
    }

    @Override
    public String getValueFromTuple(ITuple iTuple) {
        Commodity commodity = JackJsonUtil.toBean(iTuple.getStringByField("commodity_json"),Commodity.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pay_num",iTuple.getIntegerByField("pay_num"));
        jsonObject.put("amount",commodity.getPrice());
        return jsonObject.toJSONString();
    }
}
