package com.codecoord.rocketmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.codecoord.rocketmq.constant.RocketMqBizConstant;
import com.codecoord.rocketmq.domain.RocketMqEntityMessage;
import com.codecoord.rocketmq.template.OrderMessageTemplate;
import com.codecoord.rocketmq.template.RocketMqTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 消息发送
 *
 * @author xu.ze.wei
 * @since 2022/6/16
 */
@RestController
@RequestMapping("/rocketmq")
@Slf4j
public class RocketMqController {
    /**
     * 注意此处注入的是封装的RocketMqTemplate
     */
    @Resource
    private RocketMqTemplate rocketMqTemplate;
    /**
     * 注入对应业务的模板类
     */
    @Resource
    private OrderMessageTemplate orderMessageTemplate;

    /**
     * 通过实体类发送消息，发送注意事项请参考实体类
     * 说明：也可以在RocketMqTemplate按照业务封装发送方法，这样只需要调用方法指定基础业务消息接口
     */
    @RequestMapping("/entity/message")
    public Object sendMessage() {
        RocketMqEntityMessage message = new RocketMqEntityMessage();
        // 设置业务key
        message.setKey(UUID.randomUUID().toString());
        // 设置消息来源，便于查询we年
        message.setSource("xu.ze.wei.封装测试");
        // 业务消息内容
        message.setMessage("message,当前消息发送时间为：" + LocalDateTime.now());
        // Java时间字段需要单独处理，否则会序列化失败
        message.setBirthday(LocalDate.now());
        message.setTradeTime(LocalDateTime.now());
        return rocketMqTemplate.send(RocketMqBizConstant.SOURCE_TOPIC, RocketMqBizConstant.SOURCE_TAG, message);
    }

    /**
     * 此时对于调用者而且，无需创建任何类
     * 如果某天需要调整消息发送来源，如果不封装，所有原来产生message的地方全部改
     * 如果封装了，只需要改sendOrderPaid就可以切换
     */
    @RequestMapping("/order/paid")
    public Object sendOrderPaidMessage() {
        return orderMessageTemplate.sendOrderPaid(UUID.randomUUID().toString(), "paid is complete...");
    }

    /**
     * 直接将对象进行传输，也可以自己进行json转化后传输
     */
    @RequestMapping("/messageExt/message")
    public SendResult convertAndSend() {
        // 生产中不推荐使用jsonObject传递，不看发送者无法知道传递的消息包含什么信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "messageExt");
        String destination = rocketMqTemplate.buildDestination(RocketMqBizConstant.SOURCE_TOPIC, RocketMqBizConstant.SOURCE_BROADCASTING_TAG);
        // 如果要走内部方法发送则必须要按照标准来，否则就使用原生的消息发送
        return rocketMqTemplate.getTemplate().syncSend(destination, jsonObject);
    }
}
