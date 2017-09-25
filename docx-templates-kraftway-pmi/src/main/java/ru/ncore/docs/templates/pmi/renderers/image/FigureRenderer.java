package ru.ncore.docs.templates.pmi.renderers.image;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.OutputStream;

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
        TemplateUtils.render(templatePath, wordDocumentData, JtwigModel.newModel()
            .with("title", StringEscapeUtils.escapeXml(contentData.getTitle()))
            .with("uuid", contentData.getBookmarkId())
        );
    }
}
