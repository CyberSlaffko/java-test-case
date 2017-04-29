package ru.ncore.docs.docbook;

import org.xml.sax.SAXException;
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

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Parser {
    Document document;
    org.w3c.dom.Document xmlDocument;

    public Document parse(String fileName) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource("/xsd/docbook.xsd"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setSchema(schema);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setXIncludeAware(true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmlDocument = db.parse(new File(fileName));
            xmlDocument.getDocumentElement().normalize();
            document = new Document();

            parseXmlDocument();

            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return new DocumentNotFound();
    }

    private void parseXmlDocument() {
        new DocumentInfoParser(xmlDocument).parse(document);
        new ChapterParser(xmlDocument).parse(document);
    }

}
