package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class FigureParser extends ObjectWithTitleParser {

    @Override
    protected ChapterContent.Type getChapterContentType() {
        return ChapterContent.Type.FIGURE;
    }
}
