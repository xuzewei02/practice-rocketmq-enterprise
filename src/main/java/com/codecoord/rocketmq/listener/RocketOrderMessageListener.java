package com.codecoord.rocketmq.listener;

import com.codecoord.rocketmq.constant.RocketMqBizConstant;
import com.codecoord.rocketmq.domain.RocketMqEntityMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 订单处理消息
 * 解释说明参考 {@link RocketEntityMessageListener}
 *
 * @author xu.ze.wei
 * @since 2022/5/12
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMqBizConstant.SOURCE_TOPIC,
        consumerGroup = RocketMqBizConstant.ORDER_PAID_GROUP,
        selectorExpression = RocketMqBizConstant.ORDER_PAID_TAG
)
public class RocketOrderMessageListener extends BaseMqMessageListener<RocketMqEntityMessage>
                                         implements RocketMQListener<RocketMqEntityMessage> {
    @Override
    protected String consumerName() {
        return "订单消费者";
    }

    @Override
    public void onMessage(RocketMqEntityMessage message) {
        // 注意，此时这里没有直接处理业务，而是先委派给父类做基础操作，然后父类做完基础操作后会调用子类的实际处理类型
        super.dispatchMessage(message);
    }

    @Override
    protected void handleMessage(RocketMqEntityMessage message) throws Exception {
        // 此时这里才是最终的业务处理，代码只需要处理资源类关闭异常，其他的可以交给父类重试
        System.out.println("订单业务消息处理");
    }

    @Override
    protected void overMaxRetryTimesMessage(RocketMqEntityMessage message) {
    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected int maxRetryTimes() {
        return 5;
    }

    @Override
    protected boolean isThrowException() {
        return false;
    }
}
