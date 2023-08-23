package com.codecoord.rocketmq.listener;

import com.codecoord.rocketmq.constant.RocketMqBizConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 广播消息
 * 应用场景：多租户或者服务有内部缓存需要刷新情况下如果需要刷新租户信息或者缓存信息
 *      也就是需要所有服务节点都需要同事做某一件事情的时候
 * 此时可以借助广播消息发送消息到所有节点刷新，无需一个节点一个节点的处理
 *
 * 特别说明：广播消息默认会在家目录下创建消费进度文件，会以www.tianxincoord.com:9876@www.tianxincoord.com:9876
 *      这种地址形式生成文件路径，但是由于带有:符号，windows下是不允许此符号作为文件夹名称的
 *      所以如果rocketMQ的链接地址不是连接串(不带有端口)可以取消下面的messageModel注释
 *      否则启动的时候就会提示目标卷或者路径不存在，其实是因为这个问题
 *
 * @author xu.ze.wei
 * @since 2022/5/12
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMqBizConstant.SOURCE_TOPIC,
        consumerGroup = RocketMqBizConstant.SOURCE_BROADCASTING_GROUP,
        selectorExpression = RocketMqBizConstant.SOURCE_BROADCASTING_TAG
        // messageModel = MessageModel.BROADCASTING
)
public class RocketBroadcastingListener implements RocketMQListener<MessageExt> {

    /**
     * MessageExt：内置的消息实体，生产中根据需要自己封装实体
     */
    @Override
    public void onMessage(MessageExt message) {
        log.info("收到广播消息【{}】", new String(message.getBody()));
    }
}
