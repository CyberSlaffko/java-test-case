package ru.ncore.docs.templates.pmi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.Parser;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Это не тесты. Это заглушки для рендеринга всех существующих документов
 */
public class DocxMakerTest {
    @Test
    public void renderPMI01() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\PMI\\PMI-04\\PMI2017.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПМИ 01 - Предварительные.docx"));
    }

    @Test
    public void renderPz01() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Общие требования\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 01 - Общие требования.docx"));
    }

    @Test
    public void renderPz02() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Администрирование\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 02 - Администрирование.docx"));
    }

    @Test
    public void renderPz03() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Аудит действий пользователей\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 03 - Аудит действий пользователей.docx"));
    }

    @Test
    public void renderPz04() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Фильтрация\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 04 - Фильтрация.docx"));
    }

    @Test
    public void renderPz05() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Административная практика\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 05 - Административная практика.docx"));
    }

    @Test
    public void renderPz06() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Электронный журнал\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 06 - Электронный журнал.docx"));
    }

    @Test
    public void renderPz07() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Обработчик запросов\\index.xml");
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ 07 - Обработчик запросов.docx"));
    }

}
