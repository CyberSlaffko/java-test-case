package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;


/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ContentRendererFactory {
    public static IContentRenderer getRenderer(ChapterContent contentData) {
        switch (contentData.getType()) {
            case PARA: return (new ParaRenderer()).setContent(contentData);
            default: return null;
        }
    }
}
