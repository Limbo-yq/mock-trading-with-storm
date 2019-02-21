package com.limbo.actual.storm.spout;

import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;

import java.util.UUID;

public class KafkaSpoutBuilder {

    public static KafkaSpout build(String brokerZkStr,String topic){
        BrokerHosts brokerHosts = new ZkHosts(brokerZkStr);
        String id = UUID.randomUUID().toString();
        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts,topic,"/"+topic,id);
        kafkaConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        return new KafkaSpout(kafkaConfig);
    }
}
