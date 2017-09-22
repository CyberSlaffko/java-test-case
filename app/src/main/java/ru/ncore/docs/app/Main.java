package ru.ncore.docs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.ncore.docs.docbook.Parser;
import ru.ncore.docs.pdf.converter.PdfMaker;
import ru.ncore.docs.templates.pmi.DocxMaker;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 * Modified by CyberSlaffko.
 */
@SpringBootApplication
public class Main {

    @Bean
    public Parser parser() {
        return new Parser();
    }

    @Bean
    public DocxMaker docxMaker() {
        return new DocxMaker();
    }

    @Bean(destroyMethod = "cleanUp")
    public PdfMaker pdfMaker() {
        return new PdfMaker();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}