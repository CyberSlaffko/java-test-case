package ru.ncore.docs.templates.pmi.renderers;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Рендерер для картинки с заголовоком
 */
public class FigureRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document, relationManager);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }

        String templatePath = "templates/document/figure_title.twig";
        TemplateUtils.render(templatePath, wordDocumentData, new HashMap<String,String>() {{
            put("title", StringEscapeUtils.escapeXml(contentData.getTitle()));
            put("uuid", contentData.getBookmarkId());
        }});
    }
}
