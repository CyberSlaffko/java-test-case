package ru.ncore.docs.docbook.parser.table;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.ObjectParser;

/**
 * Парсер тела таблицы
 */
public class TBodyParser extends ObjectParser {
    public TBodyParser() {
        super(ChapterContent.Type.TABLE_BODY);
    }

    @Override
    protected void parseChilds(ChapterContent content) {
        chapterType = ChapterContent.ChapterType.TABLE_BODY;
        super.parseChilds(content);
    }
}
