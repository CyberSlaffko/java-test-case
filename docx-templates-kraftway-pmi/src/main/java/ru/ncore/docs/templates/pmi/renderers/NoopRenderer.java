package ru.ncore.docs.templates.pmi.renderers;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class NoopRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }
}
