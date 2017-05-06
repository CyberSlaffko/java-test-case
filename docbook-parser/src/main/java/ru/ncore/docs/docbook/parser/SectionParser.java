package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ObjectWithTitleParserAlgorithm;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.SECTION;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class SectionParser extends ObjectWithTitleParserAlgorithm {
    protected ChapterContent.Type getType() {
        return SECTION;
    }
}

