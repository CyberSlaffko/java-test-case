package ru.ncore.docs.docbook.parser.algorithms;

import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public abstract class LeafContentParserAlgorithm extends ContentParserAlgorithm {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        ChapterContent para = new ChapterContent();
        para.setLevel(currentLevel);
        para.setChapterType(chapterType);

        para.setType(getType());
        para.setTitle(getTitle());

        parseAttributes(para);

        return para;
    }

    @Override
    protected void parseChilds(ChapterContent content) {
        // У этих элементов не должно быть дочерних узлов. Они концевые
    }

    protected void parseAttributes(ChapterContent content) {
        // По умолчанию мы не парсим аттрибуты. Они не нужны на концевых элементах
    }

    protected abstract String getTitle();

    protected abstract ChapterContent.Type getType();
}
