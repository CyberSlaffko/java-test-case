package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ContentParserAlgorithm;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class ObjectParser extends ContentParserAlgorithm {
    private final ChapterContent.Type type;

    public ObjectParser(ChapterContent.Type type) {
        this.type = type;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return type;
    }
}
