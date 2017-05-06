package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.DocumentInfo;
import ru.ncore.docs.docbook.utils.XMLUtils;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class DocumentInfoParser {
    org.w3c.dom.Document xmlDocument;

    public DocumentInfoParser(org.w3c.dom.Document xmlRoot) {
        this.xmlDocument = xmlRoot;
    }

    public void parse(Document document) {
        DocumentInfo info = new DocumentInfo();
        info.setTitle(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:title/text()"));
        info.setTitleAbbrev(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:titleabbrev/text()"));
        info.setProductName(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:productname/text()"));
        info.setSubTitle(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:subtitle/text()"));
        info.setContractNum(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:contractnum/text()"));
        info.setIssueNum(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:issuenum/text()"));
        info.setPubDate(XMLUtils.getNodeValue(xmlDocument, "/d:book/d:info/d:pubdate/text()"));

        document.setInfo(info);
    }
}
