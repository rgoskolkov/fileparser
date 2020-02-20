package com.cardpay.parser.fileparser.writer;

import com.cardpay.parser.fileparser.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderWriter {
    //todo converter
    public void writeOrder(Order order) {
        System.out.println(String.format(
                "{\"id\":%d, \"amount\":%s," +
                " \"comment\":%s, \"filename\":%s, " +
                ", \"line\":%d, , \"result\":%s}",
                order.getId(),
                order.getAmount().toPlainString(),
                order.getComment(),
                order.getFileName(),
                order.getRowNum(),
                order.getResult())
        );
    }
}
