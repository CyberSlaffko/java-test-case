package ru.ncore.docs.docbook.parser.table;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.ObjectParser;

/**
 * Парсер заголовка таблицы
 */
public class THeadParser extends ObjectParser {
    public THeadParser() {
        super(ChapterContent.Type.TABLE_HEAD);
    }

    @Override
    protected void parseChilds(ChapterContent content) {
        chapterType = ChapterContent.ChapterType.TABLE_HEAD;
        super.parseChilds(content);
    }
}
