package com.limbo.actual.storm;

import com.limbo.actual.storm.bolt.TestBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

public class MyActualTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        BrokerHosts brokerHosts = new ZkHosts("storm001:2181");
        String id = UUID.randomUUID().toString();
        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts,"mytopic","/mytopic",id);
        kafkaConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        KafkaSpout kafkaSpout = new KafkaSpout(kafkaConfig);

        String spoutId = KafkaSpout.class.getSimpleName();
        builder.setSpout(spoutId,kafkaSpout);


        String bolt1 = TestBolt.class.getSimpleName();
        builder.setBolt(bolt1, new TestBolt()).shuffleGrouping(spoutId);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("MyActualTopology",new Config(),builder.createTopology());
    }
}
