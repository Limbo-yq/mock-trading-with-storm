package com.limbo.actual.storm.util;

import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JdbcUtil {

    public static class HikariCPConfigMapBuilder extends HashMap{

        public static Map build(){
            Map<String,Object> hikariCPConfigMap = new HashMap();
            hikariCPConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikariCPConfigMap.put("dataSource.url", "jdbc:mysql://localhost/db_mock_trading");
            hikariCPConfigMap.put("dataSource.user","root");
            hikariCPConfigMap.put("dataSource.password","123456");
            return hikariCPConfigMap;
        }
    }

    public static class ConnectionProviderBuilder{

        public static ConnectionProvider build(){
            return new HikariCPConnectionProvider(HikariCPConfigMapBuilder.build());
        }
    }

    public static class JdbcMapperBuilder{

        public static JdbcMapper build(String tableName){
            return new SimpleJdbcMapper(tableName,ConnectionProviderBuilder.build());
        }

        public static JdbcMapper build(String tableName,ConnectionProvider connectionProvider){
            return new SimpleJdbcMapper(tableName,connectionProvider);
        }

        public static JdbcMapper build(Column... columns){
            return new SimpleJdbcMapper(Arrays.asList(columns));
        }
    }
}
