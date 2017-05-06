package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ChapterParserAlgorithm;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class AppendixParser extends ChapterParserAlgorithm {

    public AppendixParser(Document xmlDocument) {
        super(xmlDocument);
    }

    @Override
    protected List<ChapterContent> getDataList(ru.ncore.docs.docbook.Document document) {
        return document.getAppendicesList();
    }

    @Override
    protected String xpath() {
        return "/d:book/d:appendix";
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.APPENDIX;
    }

    @Override
    protected ChapterContent.ChapterType getChapterType() {
        return ChapterContent.ChapterType.APPENDIX;
    }
}
