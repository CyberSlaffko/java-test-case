package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class PlainTextParser extends LeafContentParserAlgorithm {
    @Override
    protected String getTitle() {
        return xmlDocument.getNodeValue().replaceAll("\\s+", " ").replaceAll("\\A\\s+\\z", "");
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TEXT;
    }
}
