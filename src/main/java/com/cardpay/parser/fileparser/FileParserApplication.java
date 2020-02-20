package com.cardpay.parser.fileparser;

import com.cardpay.parser.fileparser.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SpringBootApplication
public class FileParserApplication implements CommandLineRunner {
    @Autowired
    private FileProducer fileProducer;
    @Autowired
    private OrderConsumer orderConsumer;
    @Autowired
    private Order poisonedOrder;
    @Resource(name = "orderQueue")
    private BlockingQueue<Order> ordersQueue;
    @Value("${consumers.count:2}")
    private int consumersCount;

    public static void main(String[] args) {
        SpringApplication.run(FileParserApplication.class, args).close();
    }

    @Override
    public void run(String... args) {
        Stream<CompletableFuture<Void>> producersFutures = Stream.of(args)
                .map(filename -> new File(System.getProperty("user.dir"), filename))
                .map(file -> fileProducer.startForFile(file));
        Stream<CompletableFuture<Void>> consumersFutures = Stream.generate(() -> orderConsumer.start()).limit(consumersCount);
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
