package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ChapterParserAlgorithm;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterParser extends ChapterParserAlgorithm {

    public ChapterParser(org.w3c.dom.Document xmlDocument) {
        super(xmlDocument);
    }

    @Override
    protected List<ChapterContent> getDataList(Document document) {
        return document.getChaptersList();
    }

    @Override
    protected String xpath() {
        return "/d:book/d:chapter|/d:book/d:glossary";
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.CHAPTER;
    }

    @Override
    protected ChapterContent.ChapterType getChapterType() {
        return ChapterContent.ChapterType.CHAPTER;
    }
}
