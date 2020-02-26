package com.github.myproject.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : dongma
 * Date: 2020-01-20
 * Time: 10:06
 * @version : 1.0
 * @since : JDK 1.8
 * Description: Describe the specific role of this class
 */
@Component
public class RedLockHelper {

    @Value("${redis.cluster.nodes}")
    private String clusterNodes;

    private static RedissonClient redissonClient;
    private static Config config = new Config();


    @PostConstruct
    private void init() {
        config.useClusterServers()
                .addNodeAddress(clusterNodes.split(","))
                .setConnectTimeout(30000)
                .setTimeout(1000)
                .setRetryAttempts(3);
        redissonClient = Redisson.create(config);
    }

    public static Redisson getRedisson() {
        return (Redisson) redissonClient;
    }

}