package com.cardpay.parser.fileparser;

import com.cardpay.parser.fileparser.model.Order;
import com.cardpay.parser.fileparser.parser.ParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@Component
public class FileProducer {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileProducer.class);
    @Autowired
    private ParserFactory parserFactory;
    @Resource(name = "orderQueue")
    private BlockingQueue<Order> queue;

    @Async
    public CompletableFuture<Void> startForFile(File file) {
        parserFactory.getParser(file).ifPresent(parser -> {
                    Order order = parser.getNext();
                    while (order != null) {
                        try {
                            queue.put(order);
                            order = parser.getNext();
                        } catch (InterruptedException ignored) {
                            LOGGER.warn("File producer thread interrupted");
                        }
                    }
                }
        );
        return CompletableFuture.completedFuture(null);
    }
}
