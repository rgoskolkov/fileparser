package com.cardpay.parser.fileparser;

import com.cardpay.parser.fileparser.model.Order;
import com.cardpay.parser.fileparser.writer.OrderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderConsumer {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);
    @Autowired
    private OrderWriter writer;
    @Resource(name = "orderQueue")
    private BlockingQueue<Order> orderQueue;
    @Autowired
    private Order poisonOrder;

    @Async
    public CompletableFuture<Void> start() {
        while (true) {
            try {
                Order takedOrder = orderQueue.take();
                if (takedOrder == poisonOrder) {
                    return CompletableFuture.completedFuture(null);
                }
                writer.writeOrder(takedOrder);
            } catch (InterruptedException e) {
                LOGGER.warn("Order consumer thread interrupted");
                return CompletableFuture.completedFuture(null);
            }
        }
    }
}
