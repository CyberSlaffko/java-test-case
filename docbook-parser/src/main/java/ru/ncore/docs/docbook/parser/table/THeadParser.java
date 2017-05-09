package ru.ncore.docs.docbook.parser.table;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.IContentParser;
import ru.ncore.docs.docbook.parser.ObjectParser;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
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
