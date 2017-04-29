package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public interface IContentParser {
    IContentParser setNode(Node xmlDocument);
    ChapterContent parse(int currentLevel);
}
