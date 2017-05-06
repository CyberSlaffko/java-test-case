package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public abstract class IContentParser {
    protected Node xmlDocument;
    protected Document document;

    public IContentParser setNode(Node xmlDocument) {
        this.xmlDocument = xmlDocument;
        return this;
    }

    abstract public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType);

    public IContentParser setDocument(Document document) {
        this.document = document;
        return this;
    }
}
