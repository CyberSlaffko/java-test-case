package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public abstract class IContentParser {
    public Node xmlDocument;

    public IContentParser setNode(Node xmlDocument) {
        this.xmlDocument = xmlDocument;
        return this;
    }

    abstract ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType);
}
