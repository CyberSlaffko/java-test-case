package ru.ncore.docs.templates.pmi.renderers;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;

import java.io.OutputStream;

/**
 * Специальный рендерер, который игнорирует текущий уровень и начинает рендерить своих наследников
 */
public class NoopRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document, relationManager);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }
}
