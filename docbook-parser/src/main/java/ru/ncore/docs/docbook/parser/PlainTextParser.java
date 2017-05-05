package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class PlainTextParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
            ChapterContent para = new ChapterContent();
            para.setType(ChapterContent.Type.TEXT);
            para.setLevel(currentLevel);
            para.setChapterType(chapterType);
            para.setTitle(xmlDocument.getNodeValue().replaceAll("\\s+", " ").replaceAll("\\A\\s+\\z", ""));
            return para;
    }
}
