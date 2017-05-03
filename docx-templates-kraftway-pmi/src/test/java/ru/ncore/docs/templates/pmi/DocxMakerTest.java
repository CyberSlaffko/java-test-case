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
    public void makeAPTest() throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/PMI/PMI-04/PMI2017.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());
        DocxMaker maker = new DocxMaker();
        maker.makeDocument(parseResult, java.nio.file.Paths.get("D:\\Projects\\docs\\out\\ПМИ ПИ.docx"));
    }
}
