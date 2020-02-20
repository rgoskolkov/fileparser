package com.cardpay.parser.fileparser.model;

import java.math.BigDecimal;

public class Order {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long rowNum;
    private String result;

    public Order() {
    }

    public Order(Long id, BigDecimal amount, String currency, String comment, String fileName, Long rowNum) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.fileName = fileName;
        this.rowNum = rowNum;
        this.result = "ок";
    }

    public Order(String result, String fileName, Long rowNum) {
        this.fileName = fileName;
        this.result = result;
        this.rowNum = rowNum;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getComment() {
        return comment;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getRowNum() {
        return rowNum;
    }

    public String getResult() {
        return result;
    }
}
