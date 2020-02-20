package com.cardpay.parser.fileparser.parser;

import com.cardpay.parser.fileparser.model.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class CSVParser extends OrderParser {
    private String cvsSplitBy = ",";
    private Long rowNum = 0L;

    public CSVParser(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public Order getNext() {
        try {
            String line = reader.readLine();
            if (line != null) {
                rowNum ++;
                String[] order = line.split(cvsSplitBy);
                return new Order(Long.parseLong(order[0]),
                        BigDecimal.valueOf(Double.parseDouble(order[1])),
                        order[2],
                        order[3],
                        file.getName(),
                        rowNum
                        );
            } else {
                reader.close();
                return null;
            }
        } catch (NumberFormatException | IOException e) {
            return new Order(e.getLocalizedMessage(), file.getName(), rowNum);
        }
    }
}
