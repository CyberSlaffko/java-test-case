package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ObjectWithTitleParserAlgorithm;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class TableParser extends ObjectWithTitleParserAlgorithm {
    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TABLE;
    }
}
