package com.codecoord.rocketmq.constant;

/**
 * RocketMQ业务常量
 *
 *
 * @author xu.ze.wei
 * @since 2022/5/18
 */
public interface RocketMqBizConstant {
    /**
     * 先在服务器创建好TOPIC
     */
    String SOURCE_TOPIC = "rocketmq_source_code_topic";
    String SOURCE_GROUP = "rocketmq_source_code_group";
    String SOURCE_TAG = "rocketmq_source_code_tag";
    /**
     * 广播消息
     */
    String SOURCE_BROADCASTING_GROUP = "rocketmq_broadcasting_group";
    String SOURCE_BROADCASTING_TAG = "rocketmq_broadcasting_tag";
    /**
     * 订单支付
     */
    String ORDER_PAID_GROUP = "order_paid_GROUP";
    String ORDER_PAID_TAG = "order_paid_express";
}
