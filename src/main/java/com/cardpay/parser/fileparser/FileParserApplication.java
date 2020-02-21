package com.cardpay.parser.fileparser;

import com.cardpay.parser.fileparser.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class FileParserApplication implements CommandLineRunner {
    private final FileProducer fileProducer;
    private final OrderConsumer orderConsumer;
    private final Order poisonedOrder;
    @Resource(name = "orderQueue")
    private BlockingQueue<Order> ordersQueue;
    @Value("${consumers.count:2}")
    private int consumersCount;

    public FileParserApplication(FileProducer fileProducer, OrderConsumer orderConsumer, Order poisonedOrder) {
        this.fileProducer = fileProducer;
        this.orderConsumer = orderConsumer;
        this.poisonedOrder = poisonedOrder;
    }

    public static void main(String[] args) {
        SpringApplication.run(FileParserApplication.class, args).close();
    }

    @Override
    public synchronized void run(String... args) {
        Set<CompletableFuture<Void>> producersFutures = Stream.of(args)
                .map(filename -> new File(System.getProperty("user.dir"), filename))
                .map(fileProducer::startForFile)
                .collect(Collectors.toSet());

        Set<CompletableFuture<Void>> consumersFutures = Stream.generate(orderConsumer::start)
                .limit(consumersCount)
                .collect(Collectors.toSet());
        producersFutures.forEach(CompletableFuture::join);
        sendPoison();
        consumersFutures.forEach(CompletableFuture::join);
    }

    private void sendPoison() {
        for (int i = 0; i < consumersCount; i++) {
            try {
                ordersQueue.put(poisonedOrder);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
