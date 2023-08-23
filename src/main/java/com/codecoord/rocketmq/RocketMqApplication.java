package com.codecoord.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RocketMQ启动类
 *
 * @author tianxincode@163.com
 * @since 2022/6/16
 */
@SpringBootApplication
public class RocketMqApplication {
    public static void main(String[] args) {
        /*
         * 指定使用的日志框架，否则将会报错
         * RocketMQLog:WARN No appenders could be found for logger (io.netty.util.internal.InternalThreadLocalMap).
         * RocketMQLog:WARN Please initialize the logger system properly.
         */
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        SpringApplication.run(RocketMqApplication.class, args);
    }
}
