package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterParser extends ChapterParserStrategy  {

    public ChapterParser(org.w3c.dom.Document xmlDocument) {
        super(xmlDocument);
    }

    @Override
    protected List<Chapter> getDataList(Document document) {
        return document.getChaptersList();
    }

    @Override
    protected String xpath() {
        return "/d:book/d:chapter";
    }

    @Override
    protected ChapterContent.ChapterType getChapterType() {
        return ChapterContent.ChapterType.CHAPTER;
    }
}
