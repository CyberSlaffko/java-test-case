package ru.ncore.docs.templates.pmi.numbering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberingExtender {
    final static private Logger logger = LoggerFactory.getLogger(NumberingExtender.class);
    ru.ncore.docs.docbook.Document document;
    FileSystem zipfs;
    org.w3c.dom.Document xmlDoc;
    String xmlFileName;

    public NumberingExtender(ru.ncore.docs.docbook.Document document, FileSystem zipfs, String xmlFileName) {
        this.document = document;
        this.zipfs = zipfs;
        this.xmlFileName = xmlFileName;
        this.xmlDoc = this.openXML();
    }

    private int findMaxAbstractNum() {
        int ret = 0;
        NodeList items = xmlDoc.getElementsByTagName("w:abstractNum");
        if (items.getLength() > 0) {
            Node n = items.item(items.getLength() - 1);
            ret = Integer.parseInt(n.getAttributes().getNamedItem("w:abstractNumId").getTextContent());
        }
        return ret;
    }

    private int findMaxNum() {
        int ret = 0;
        NodeList items = xmlDoc.getElementsByTagName("w:num");
        if (items.getLength() > 0) {
            Node n = items.item(items.getLength() - 1);
            ret = Integer.parseInt(n.getAttributes().getNamedItem("w:numId").getTextContent());
        }
        return ret;
    }

    private void calcOL(ChapterContent chapter, AtomicInteger idAbstractNum, AtomicInteger idNum) {
        if (chapter.getType() == ChapterContent.Type.ORDEREDLIST) {
            int idA = idAbstractNum.incrementAndGet();
            int idN = idNum.incrementAndGet();
            this.addNumbering(idA, idN);
            chapter.addAdditionalAttribute("num", Integer.toString(idN));
        }
        for (ChapterContent ch : chapter.getContentList()) {
            this.calcOL(ch, idAbstractNum, idNum);
        }
    }


    public void calc() {
        AtomicInteger idAbstractNum = new AtomicInteger(this.findMaxAbstractNum());
        AtomicInteger idNum = new AtomicInteger(this.findMaxNum());

        for (ChapterContent ch : document.getChaptersList()) {
            this.calcOL(ch, idAbstractNum, idNum);
        }

        NodeList nl = this.xmlDoc.getElementsByTagName("w:nsid");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            n.getParentNode().removeChild(n);
        }

        NodeList its = this.xmlDoc.getElementsByTagName("w:numIdMacAtCleanup");
        Node cleanup = its.item(0);
        cleanup.getAttributes().getNamedItem("w:val").setNodeValue(idNum.toString());

    }

    private void addNumbering(int idAbstractNum, int idNum) {

        NodeList its = this.xmlDoc.getElementsByTagName("w:abstractNum");
        Node n = its.item(0).cloneNode(true);
        Node lastAbstractNum = its.item(its.getLength() - 1);
        n.getAttributes().getNamedItem("w:abstractNumId").setNodeValue(Integer.toString(idAbstractNum));
        NodeList items = this.xmlDoc.getElementsByTagName("w:numbering");
        Node root = items.item(0);
        root.insertBefore(n, lastAbstractNum.getNextSibling());

        its = this.xmlDoc.getElementsByTagName("w:num");
        Node nn = its.item(0).cloneNode(true);
        nn = nn.cloneNode(true);
        Node lastNum = its.item(its.getLength() - 1);

        nn.getAttributes().getNamedItem("w:numId").setNodeValue(Integer.toString(idNum));
        nn.getFirstChild().getAttributes().getNamedItem("w:val").setNodeValue(Integer.toString(idAbstractNum));
        root.insertBefore(nn, lastNum.getNextSibling());
    }

    private org.w3c.dom.Document openXML() {
        org.w3c.dom.Document doc = null;
        try (InputStream inputStream = Files.newInputStream(zipfs.getPath(this.xmlFileName), StandardOpenOption.READ)) {
            try {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                doc = documentBuilder.parse(inputStream);
                doc.normalize();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                logger.error(e.getMessage());
            }
        } catch (IOException e) {
            logger.info("Cannot read " + this.xmlFileName);
        }
        return doc;
    }


    public void save() {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance()
                    .newTransformer();
            DOMSource source = new DOMSource(this.xmlDoc);
            ByteArrayOutputStream numberingData = new ByteArrayOutputStream(10240);
            StreamResult result = new StreamResult(numberingData);
            transformer.transform(source, result);
            Files.copy(new ByteArrayInputStream(numberingData.toByteArray()), zipfs.getPath(this.xmlFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
