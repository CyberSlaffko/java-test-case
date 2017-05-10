package ru.ncore.docs.templates.pmi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.Parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Это не тесты. Это заглушки для рендеринга всех существующих документов
 */
public class DocxMakerTest {
    @Test
    public void renderPMI01() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\PMI\\PMI-04\\PMI2017.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz01() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Общие требования\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz02() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Администрирование\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz03() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Аудит действий пользователей\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz04() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Фильтрация\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz05() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Административная практика\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz06() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Электронный журнал\\index.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderPz07() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\TP_2017\\Обработчик запросов\\index.xml");
        makeDoc(resPath);
    }


    @Test
    public void renderRo01() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\Opisanie_primeneniya\\Opisanie_primeneniya.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo02() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\Opisanie_programmi\\Opisanie_programmi.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo03() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\PEO\\PEO.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo04() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\Programma_obuch\\Programma_obucheniya.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo05() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\Ryk_Programmista\\Ruk_programmista.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo06() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\Ryk_po_sborke\\Ruk_po_sborke.xml");
        makeDoc(resPath);
    }

    @Test
    public void renderRo07() throws URISyntaxException, IOException, InvalidFormatException {
        java.nio.file.Path resPath = java.nio.file.Paths.get("D:\\Projects\\ibd-docs\\Doc_2017-2018\\RO_GZ\\gos_zashita.xml");
        makeDoc(resPath);
    }

    private void makeDoc(Path resPath) throws URISyntaxException, IOException, InvalidFormatException {
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get(String.format("D:\\Projects\\docs\\out\\%s - %s.docx",
                parseResult.getInfo().getIssueNum(), parseResult.getInfo().getSubTitle())));
    }
}
