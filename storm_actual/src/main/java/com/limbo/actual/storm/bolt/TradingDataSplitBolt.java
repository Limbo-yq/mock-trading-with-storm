package com.limbo.actual.storm.bolt;

import com.limbo.actual.mocktrading.model.Commodity;
import com.limbo.actual.mocktrading.model.Person;
import com.limbo.actual.mocktrading.model.TradingDetail;
import com.limbo.actual.mocktrading.util.JackJsonUtil;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class TradingDataSplitBolt extends BaseRichBolt {
    private OutputCollector outputCollector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String tradingJson = new String(input.getBinaryByField("bytes"));
            TradingDetail trading = JackJsonUtil.toBean(tradingJson, TradingDetail.class);

            Person payer = trading.getPayer();
            Person payee = trading.getPayee();
            Commodity commodity = trading.getCommodity();
            Integer payNum = trading.getPayNumber();
            Integer payStatus = trading.getPayStatus();

            this.outputCollector.emit(
                    new Values(
                            payNum,
                            payStatus,
                            JackJsonUtil.toJson(payer),
                            JackJsonUtil.toJson(payee),
                            JackJsonUtil.toJson(commodity),
                            tradingJson
                    )
            );
            this.outputCollector.ack(input);
        }catch (Exception e){
            this.outputCollector.fail(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("pay_num", "pay_status", "payer_json", "payee_json", "commodity_json","trading"));
    }
}
