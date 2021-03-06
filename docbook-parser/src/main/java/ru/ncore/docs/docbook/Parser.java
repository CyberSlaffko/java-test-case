package ru.ncore.docs.docbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ru.ncore.docs.docbook.parser.AnnotationParser;
import ru.ncore.docs.docbook.parser.AppendixParser;
import ru.ncore.docs.docbook.parser.ChapterParser;
import ru.ncore.docs.docbook.parser.DocumentInfoParser;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Parser {
    final static Logger logger = LoggerFactory.getLogger(Parser.class);

    Document document;
    org.w3c.dom.Document xmlDocument;

    public Document parse(String uri) {
        logger.debug(String.format("Parsing file: %s", uri));
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource("/xsd/docbook.xsd"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setSchema(schema);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setXIncludeAware(true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmlDocument = db.parse(uri);
            xmlDocument.getDocumentElement().normalize();
            document = new Document(uri);

            parseXmlDocument();

            return document;
        } catch (ParserConfigurationException e) {
            logger.error("Something went wrong", e);
        } catch (IOException e) {
            logger.error("Something went wrong", e);
        } catch (SAXException e) {
            logger.error("Something went wrong", e);
        }
        return new DocumentNotFound();
    }

    private void parseXmlDocument() {
        AtomicInteger seq = new AtomicInteger(0);
        new DocumentInfoParser(xmlDocument).parse(document);
        new AnnotationParser(xmlDocument).parse(document);
        new ChapterParser(xmlDocument).parse(document, seq);
        new AppendixParser(xmlDocument).parse(document, seq);
    }
}
