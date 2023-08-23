package com.codecoord.rocketmq.domain;

import com.codecoord.rocketmq.config.RocketMqConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * RocketMQ实体类消息
 *
 * @author xu.ze.wei
 * @since 2022/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RocketMqEntityMessage extends BaseMqMessage {
    /**
     * 业务消息
     */
    private String message;
    /**
     * ....更多业务字段根据业务添加
     *
     * LocalDate和LocalDateTime默认不支持，需要单独处理
     * {@link RocketMqConfig}
     */
    private LocalDate birthday;
    private LocalDateTime tradeTime;
}
