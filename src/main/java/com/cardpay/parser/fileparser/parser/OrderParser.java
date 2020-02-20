package com.cardpay.parser.fileparser.parser;


import com.cardpay.parser.fileparser.model.Order;

import java.io.*;

public abstract class OrderParser {
    protected File file;
    protected BufferedReader reader;

    public OrderParser(File file) throws FileNotFoundException {
        this.file = file;
        this.reader = new BufferedReader(new FileReader(file));
    }

    public abstract Order getNext();
}
