package com.codecoord.rocketmq.listener;

import com.codecoord.rocketmq.constant.RocketMqBizConstant;
import com.codecoord.rocketmq.domain.RocketMqEntityMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 实体类消费监听器，在实现RocketMQListener中间还加了一层BaseMqMessageListener来处理基础业务消息
 *
 * @author xu.ze.wei
 * @since 2022/5/12
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMqBizConstant.SOURCE_TOPIC,
        consumerGroup = RocketMqBizConstant.SOURCE_GROUP,
        selectorExpression = RocketMqBizConstant.SOURCE_TAG,
        // 指定消费者线程数，默认64，生产中请注意配置，避免过大或者过小
        consumeThreadMax = 5
)
public class RocketEntityMessageListener extends BaseMqMessageListener<RocketMqEntityMessage>
                                         implements RocketMQListener<RocketMqEntityMessage> {
    /**
     * 此处只是说明封装的思想，更多还是要根据业务操作决定
     * 内功心法有了，无论什么招式都可以发挥最大威力
     */
    @Override
    protected String consumerName() {
        return "RocketMQ二次封装消息消费者";
    }

    @Override
    public void onMessage(RocketMqEntityMessage message) {
        // 注意，此时这里没有直接处理业务，而是先委派给父类做基础操作，然后父类做完基础操作后会调用子类的实际处理类型
        super.dispatchMessage(message);
    }

    @Override
    protected void handleMessage(RocketMqEntityMessage message) throws Exception {
        // 此时这里才是最终的业务处理，代码只需要处理资源类关闭异常，其他的可以交给父类重试
        System.out.println("业务消息处理");
    }

    @Override
    protected void overMaxRetryTimesMessage(RocketMqEntityMessage message) {
        // 当超过指定重试次数消息时此处方法会被调用
        // 生产中可以进行回退或其他业务操作
    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected int maxRetryTimes() {
        // 指定需要的重试次数，超过重试次数overMaxRetryTimesMessage会被调用
        return 5;
    }

    @Override
    protected boolean isThrowException() {
        // 是否抛出异常，到消费异常时是被父类拦截处理还是直接抛出异常
        return false;
    }
}
