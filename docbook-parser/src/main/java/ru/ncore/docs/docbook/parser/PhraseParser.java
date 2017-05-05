package ru.ncore.docs.docbook.parser;

import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class PhraseParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        ChapterContent para = new ChapterContent();
        para.setType(ChapterContent.Type.PHRASE);
        para.setLevel(currentLevel);
        para.setChapterType(chapterType);
        para.setTitle(XMLUtils.getNodeValue(xmlDocument, "./text()"));
        return para;
    }
}