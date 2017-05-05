package ru.ncore.docs.docbook.parser;

import org.w3c.dom.*;
import org.w3c.dom.Document;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class AppendixParser extends ChapterParserStrategy {

    public AppendixParser(Document xmlDocument) {
        super(xmlDocument);
    }

    @Override
    protected List<Chapter> getDataList(ru.ncore.docs.docbook.Document document) {
        return document.getAppendiciesList();
    }

    @Override
    protected String xpath() {
        return "/d:book/d:appendix";
    }

    @Override
    protected ChapterContent.ChapterType getChapterType() {
        return ChapterContent.ChapterType.APPENDIX;
    }
}
