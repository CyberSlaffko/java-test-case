package ru.ncore.docs.pdf.converter;

import org.apache.xmlgraphics.util.MimeConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testable
class PdfMakerTest {

    private static PdfMaker pdfMaker = null;
    private static InputStream xmlEmptyInputStream;

    @BeforeAll
    static void initPdfMaker() {
        pdfMaker = new PdfMaker();
        xmlEmptyInputStream = new ByteArrayInputStream(PdfMaker.EMPTY_XML.getBytes());
    }

    @Test
    public void xsltTransformTest()throws Exception{
        InputStream xsl = getClass().getResourceAsStream("/xml/simple.xml");
        assertNotNull(xsl);
        xmlEmptyInputStream.reset();
        Source xmlSource = new StreamSource(xmlEmptyInputStream);
        Source xslSource = new StreamSource(xsl);
        final Map<String,Object> data = new HashMap<String,Object>(){{
            put("local.value","VALUE");
        }};
        Map<String,Object> param = new HashMap<String,Object>(){{
            put("data",data);
        }};
        ByteArrayOutputStream xsltOutputStream = new ByteArrayOutputStream();
        Result xsltResult = new StreamResult(xsltOutputStream);
        PdfMaker.xsltProcess(xmlSource, xslSource, param, xsltResult);
        String xsltOutput = xsltOutputStream.toString();
        assertNotNull(xsltOutput);
        assertTrue(xsltOutput.contains("VALUE"));
    }

    @Test
    @Disabled
    public void generatePdfBySampleFo()throws Exception{
        assertNotNull(pdfMaker);
        InputStream xsl = getClass().getResourceAsStream("/xml/sample.xml");
        assertNotNull(xsl);
        String pdfFileName = System.getProperty("java.io.tmpdir")+"/sample.pdf";
        final Map<String,Object> parameters = new HashMap<String,Object>(){{
            put("data","VALUE");
            put("data_ru","Значение");
        }};
        Path pdfFilePath = Paths.get(pdfFileName);
        OutputStream out = Files.newOutputStream(pdfFilePath, StandardOpenOption.TRUNCATE_EXISTING);
        xmlEmptyInputStream.reset();
        pdfMaker.generatePdf(parameters, xmlEmptyInputStream, xsl, out);
        out.close();
        assertTrue(Files.exists(pdfFilePath));
        assertTrue(Files.isRegularFile(pdfFilePath));
        assertTrue(Files.getFileStore(pdfFilePath).getTotalSpace() > 100);
    }

    @Test
    @Disabled
    public void generatePngBySampleFo()throws Exception{
        assertNotNull(pdfMaker);
        InputStream xsl = getClass().getResourceAsStream("/xml/sample.xml");
        assertNotNull(xsl);
        String outFileName = System.getProperty("java.io.tmpdir")+"/sample.png";
        Path outFilePath = Paths.get(outFileName);
        final Map<String,Object> parameters = new HashMap<String,Object>(){{
            put("data","VALUE");
            put("data_ru","Значение");
        }};
        OutputStream out = new FileOutputStream(outFileName);
        xmlEmptyInputStream.reset();
        pdfMaker.generatePng(parameters, xmlEmptyInputStream, xsl, out);
        out.close();
        assertTrue(Files.exists(outFilePath));
        assertTrue(Files.isRegularFile(outFilePath));
        assertTrue(Files.getFileStore(outFilePath).getTotalSpace() > 100);
    }

    @Test
    public void generatePdfByDocbook()throws Exception{
        assertNotNull(pdfMaker);
        final Map<String,Object> parameters = new HashMap<String, Object>(){{
            put("body.font.family", "DejaVu Serif");
            put("sans.font.family", "Liberation Sans");
            put("title.font.family", "DejaVu Sans");
            put("monospace.font.family", "DejaVu Sans Mono");
            put("symbol.font.family", "OpenSymbol");
        }};
        Path pdfFilePath = Paths.get(".\\target\\test.pdf");
        try (
                OutputStream out = Files.newOutputStream(pdfFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        ) {
            pdfMaker.generateOut(parameters, new StreamSource(URI.create("https://bitbucket.org/Lab50/espd-docbook5/raw/d244a2fa6fc03343a0c642713ff989e1d718d9d3/%D1%88%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD%D1%8B/%D0%BF%D0%BE%D1%8F%D1%81%D0%BD%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%B0%D1%8F_%D0%B7%D0%B0%D0%BF%D0%B8%D1%81%D0%BA%D0%B0/book.xml").toASCIIString()), new StreamSource(URI.create("http://lab50.net/xsl/espd/espd.xsl").toASCIIString()), MimeConstants.MIME_PDF, out);
        }
        assertTrue(Files.exists(pdfFilePath));
        assertTrue(Files.isRegularFile(pdfFilePath));
        assertTrue(Files.getFileStore(pdfFilePath).getTotalSpace() > 100);
    }

}
