package com.cardpay.parser.fileparser;

import com.cardpay.parser.fileparser.model.Order;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableAsync
@EnableAutoConfiguration
public class SpringConfiguration {
    @Bean
    public BlockingQueue orderQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public Order poisonOrder() {
        return new Order();
    }
}
