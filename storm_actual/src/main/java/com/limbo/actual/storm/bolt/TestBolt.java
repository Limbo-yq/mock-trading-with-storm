package com.limbo.actual.storm.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class TestBolt extends BaseRichBolt {
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public void execute(Tuple input) {
        byte[] values = input.getBinaryByField("bytes");
        String value = new String(values);
        System.out.println("value = [" + value + "]");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
