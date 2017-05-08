package ru.ncore.docs.templates.pmi;

import org.junit.jupiter.api.Test;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.Parser;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class DocxMakerTest {
    @Test
    public void renderPMI() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/PMI/PMI-04/PMI2017.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПМИ ПИ.docx"));
    }

    @Test
    public void renderObTr() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/ObTr/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Общие требования.docx"));
    }

    @Test
    public void renderOz() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/OZ/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Обработчик запросов.docx"));
    }

    @Test
    public void renderEzh() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/Ezh/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Электронный журнал.docx"));
    }

    @Test
    public void renderAp() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/AP/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Адм практика.docx"));
    }

    @Test
    public void renderAu() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/AU/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Аудит.docx"));
    }

    @Test
    public void renderAd() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/TP_2017/AD/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПЗ - Админка.docx"));
    }
}
