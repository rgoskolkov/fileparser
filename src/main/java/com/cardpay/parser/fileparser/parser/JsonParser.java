package com.cardpay.parser.fileparser.parser;

import com.cardpay.parser.fileparser.model.Order;

import java.io.File;
import java.io.FileNotFoundException;

public class JsonParser extends OrderParser {
    public JsonParser(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public Order getNext() {
        return null;
    }
}
