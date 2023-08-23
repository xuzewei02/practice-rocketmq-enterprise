package com.codecoord.rocketmq.config;

import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * RocketMQ多环境隔离配置
 * 原理：对于每个配置的Bean在实例化前，拿到Bean的监听器注解把group或者topic改掉
 *
 * @author xu.ze.wei
 * @since 2022/5/18
 */
@Configuration
public class IsolationConfig implements BeanPostProcessor {
    @Value("${system.environment.isolation:true}")
    private boolean enabledIsolation;
    @Value("${system.environment.name:''}")
    private String environmentName;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean,
                                                  @NonNull String beanName) throws BeansException {
        // DefaultRocketMQListenerContainer是监听器实现类
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            // 开启消息隔离情况下获取隔离配置，隔离topic，根据自己的需求隔离group或者tag
            if (enabledIsolation && StringUtils.hasText(environmentName)) {
                container.setTopic(String.join("_", container.getTopic(), environmentName));
            }
            return container;
        }
        return bean;
    }
}
