package com.codecoord.rocketmq.template;

import com.codecoord.rocketmq.constant.RocketMqBizConstant;
import com.codecoord.rocketmq.domain.RocketMqEntityMessage;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单类发送消息模板工具类
 *
 * @author tianxincode@163.com
 * @since 2022/6/16
 */
@Component
public class OrderMessageTemplate extends RocketMqTemplate {
    /// 如果不采用继承也可以直接注入使用
    /* @Resource
    private RocketMqTemplate rocketMqTemplate; */

    /**
     * 入参只需要传入是哪个订单号和业务体消息即可，其他操作根据需要处理
     * 这样对于调用者而言，可以更加简化调用
     */
    public SendResult sendOrderPaid(@NotNull String orderId, String body) {
        RocketMqEntityMessage message = new RocketMqEntityMessage();
        message.setKey(orderId);
        message.setSource("订单支付");
        message.setMessage(body);
        // 这两个字段只是为了测试
        message.setBirthday(LocalDate.now());
        message.setTradeTime(LocalDateTime.now());
        return send(RocketMqBizConstant.SOURCE_TOPIC, RocketMqBizConstant.ORDER_PAID_TAG, message);
    }
}
