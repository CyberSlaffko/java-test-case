package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ParaParser implements IContentParser {
    private Node xmlDocument;

    @Override
    public IContentParser setNode(Node xmlDocument) {
        this.xmlDocument = xmlDocument;
        return this;
    }

    @Override
    public ChapterContent parse(int currentLevel) {
        ChapterContent para = new ChapterContent();
        para.setType(ChapterContent.Type.PARA);
        para.setLevel(currentLevel);
        para.setTitle(XMLUtils.getNodeValue(xmlDocument, "./text()"));

        return para;
    }
}
