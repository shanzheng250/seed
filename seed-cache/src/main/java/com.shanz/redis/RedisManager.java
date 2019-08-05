package com.shanz.redis;

import com.shanz.Jsons;
import com.shanz.api.cache.ICacheClient;
import com.shanz.api.spi.SPIOrder;
import com.shanz.config.RedisConfig;
import com.shanz.log.Logs;
import com.shanz.redis.connection.RedisConnectionFactory;
import com.shanz.redis.mq.Subscribe;
import com.shanz.thread.NamedThreadFactory;
import redis.clients.jedis.*;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName:RedisManager
 * @Description: redis 操作
 * @Author: shanz
 * @Date: 2019/6/18 17:03
 * @Version:1.0
 **/
@SPIOrder(order = -1)
public final class RedisManager implements ICacheClient {

    public static RedisManager instance = new RedisManager();     // redis操作实例

    private RedisConnectionFactory factory = new RedisConnectionFactory();

    /**
     * 功能描述 redis 初始化
     * @param:
     * @return:
     * @date: 2019/6/19 10:26
     */
    @Override
    public void init(){
        // todo 从配置文件中读取
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setClusterModel("");

        factory.setPassword(redisConfig.getPassword());

        factory.setPoolConfig(redisConfig.getJedisPoolConfig());

        factory.setCluster(redisConfig.isCluster());

        factory.init();
        // 测试是否成功读取redis
        test();
    }


    /**
     * 功能描述
     * @param: redis 操作没有结果返回
     * @return:
     * @date: 2019/6/19 10:31
     */
    private void call(Consumer<JedisCommands> consumer) {
        if (factory.isCluster()) {
            try {
                consumer.accept(factory.getClusterConnection());
            } catch (Exception e) {
                Logs.CACHE.error("redis ex", e);
                throw new RuntimeException(e);
            }
        } else {
            try (Jedis jedis = factory.getJedisConnection()) {
                consumer.accept(jedis);
            } catch (Exception e) {
                Logs.CACHE.error("redis ex", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 功能描述
     * @param: redis 操作有结果返回
     * @return:
     * @date: 2019/6/19 10:31
     */
    private <R> R call(Function<JedisCommands, R> function, R d) {
        if (factory.isCluster()) {
            try {
                return function.apply(factory.getClusterConnection());
            } catch (Exception e) {
                Logs.CACHE.error("redis ex", e);
                throw new RuntimeException(e);
            }
        } else {
            try (Jedis jedis = factory.getJedisConnection()) {
                return function.apply(jedis);
            } catch (Exception e) {
                Logs.CACHE.error("redis ex", e);
                throw new RuntimeException(e);
            }
        }
    }


    /*** mq 操作 ***/
    public void publish(String channel,Object object){
        String message = object instanceof String ? (String)object : Jsons.toJson(object);

        call(jedis -> {
            if (jedis instanceof MultiKeyCommands) {
                ((MultiKeyCommands) jedis).publish(channel,message);
            } else if (jedis instanceof MultiKeyJedisClusterCommands) {
                ((MultiKeyJedisClusterCommands) jedis).publish(channel,message);
            }
        });
    }


    public void subscribe(Subscribe pubsub, String channel){
        NamedThreadFactory factory = new NamedThreadFactory();
        // 启动线程
        factory.newThread(channel,() ->
                call(jedis->{
                    if (jedis instanceof MultiKeyCommands) {
                        ((MultiKeyCommands) jedis).subscribe(pubsub, channel);
                    } else if (jedis instanceof MultiKeyJedisClusterCommands) {
                        ((MultiKeyJedisClusterCommands) jedis).subscribe(pubsub, channel);
                    }
        })).start();
    }


    /**** string 操作****/
    public void set(String key, String value){
        call(jedis->{
            jedis.set(key,value);
        });
    }


    /**** set 操作****/



    /**** hash 操作****/



    /**** zset 操作****/



    /**** list 操作****/



    private void test() {
        if (factory.isCluster()) {
            JedisCluster cluster = factory.getClusterConnection();
            if (cluster == null) throw new RuntimeException("init redis cluster error.");
        } else {
            Jedis jedis = factory.getJedisConnection();
            if (jedis == null) throw new RuntimeException("init redis error, can not get connection.");
            jedis.close();
        }
    }
}
