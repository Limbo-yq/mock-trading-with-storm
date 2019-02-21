package com.limbo.actual.storm;

import com.limbo.actual.storm.bolt.TradingDataSplitBolt;
import com.limbo.actual.storm.redis.RedisStoreBoltBuilder;
import com.limbo.actual.storm.redis.TradingPayeeStoreMapper;
import com.limbo.actual.storm.redis.TradingPayerStoreMapper;
import com.limbo.actual.storm.spout.KafkaSpoutBuilder;
import com.limbo.actual.storm.util.JdbcUtil;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.topology.TopologyBuilder;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TradingFailTopology {

    public static void main(String[] args) {
        //数据源spout
        KafkaSpout kafkaSpout = KafkaSpoutBuilder.build("storm001:2181", "trading_fail");

        //mysql 存储
        JdbcInsertBolt insertBolt = new JdbcInsertBolt(
                JdbcUtil.ConnectionProviderBuilder.build(),
                JdbcUtil.JdbcMapperBuilder.build(
                        new Column("pay_num", Types.INTEGER),
                        new Column("pay_status", Types.INTEGER),
                        new Column("payer_json", Types.LONGVARCHAR),
                        new Column("payee_json", Types.LONGVARCHAR),
                        new Column("commodity_json", Types.LONGVARCHAR)
                )
        ).withTableName("trading_log").withQueryTimeoutSecs(30);

        TopologyBuilder builder = new TopologyBuilder();
        // 从kafka获取的数据源
        String spoutId = "trading_fail";
        builder.setSpout(spoutId, kafkaSpout);
        builder.setBolt("data_split", new TradingDataSplitBolt()).shuffleGrouping(spoutId);

        //记录交易信息到mysql
        builder.setBolt("trading_log_store", insertBolt).shuffleGrouping("data_split");

        try {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("TradingFailTopology", new Config(), builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
