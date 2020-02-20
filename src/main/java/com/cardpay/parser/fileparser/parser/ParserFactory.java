package com.cardpay.parser.fileparser.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

@Component
public class ParserFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserFactory.class);

    public Optional<OrderParser> getParser(File file) {
        try {
            if (file.getName().endsWith(".csv")) {
                return Optional.of(new CSVParser(file));
            } else if (file.getName().endsWith(".json")) {
                return Optional.of(new JsonParser(file));
            } else {
                LOGGER.error("Нет парсера для файла {}", file.getName());
                return Optional.empty();
            }
        } catch (FileNotFoundException exc) {
            LOGGER.error("Ошибка открытия файла {}", file.getName());
            return Optional.empty();
        }
    }
}
