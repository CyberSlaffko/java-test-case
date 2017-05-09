package ru.ncore.docs.docbook.parser.table;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ObjectWithTitleParserAlgorithm;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class TableParser extends ObjectWithTitleParserAlgorithm {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        ChapterContent parse = super.parse(currentLevel, ChapterContent.ChapterType.TABLE);
        parse.setChapterType(chapterType);
        return parse;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TABLE;
    }
}
