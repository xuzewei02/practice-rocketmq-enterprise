package com.codecoord.rocketmq.template;

import com.alibaba.fastjson.JSONObject;
import com.codecoord.rocketmq.constant.RocketMqSysConstant;
import com.codecoord.rocketmq.domain.BaseMqMessage;
import com.codecoord.rocketmq.util.JsonUtil;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * RocketMQ模板类
 *
 * @author xu.ze.wei
 * @since 2022/4/15
 */
@Component
public class RocketMqTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqTemplate.class);
    @Resource(name = "rocketMQTemplate")
    private RocketMQTemplate template;

    /**
     * 获取模板，如果封装的方法不够提供原生的使用方式
     */
    public RocketMQTemplate getTemplate() {
        return template;
    }

    /**
     * 构建目的地
     */
    public String buildDestination(String topic, String tag) {
        return topic + RocketMqSysConstant.DELIMITER + tag;
    }

    /**
     * 发送同步消息
     */
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message) {
        // 注意分隔符
        return send(topic + RocketMqSysConstant.DELIMITER + tag, message);
    }

    public <T extends BaseMqMessage> SendResult send(String destination, T message) {
        // 设置业务键，此处根据公共的参数进行处理
        // 更多的其它基础业务处理...
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = template.syncSend(destination, sendMessage);
        // 此处为了方便查看给日志转了json，根据选择选择日志记录方式，例如ELK采集
        LOGGER.info("[{}]同步消息[{}]发送结果[{}]", destination, JsonUtil.toJson(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }

    /**
     * 发送延迟消息
     */
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message, int delayLevel) {
        return send(topic + RocketMqSysConstant.DELIMITER + tag, message, delayLevel);
    }

    public <T extends BaseMqMessage> SendResult send(String destination, T message, int delayLevel) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = template.syncSend(destination, sendMessage, 3000, delayLevel);
        LOGGER.info("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, JsonUtil.toJson(message), JsonUtil.toJson(sendResult));
        return sendResult;
    }
}
