package ru.ncore.docs.templates.pmi.renderers.table;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class TableRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        renderTitle(wordDocumentData);

        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document, relationManager);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }

    private void renderTitle(OutputStream wordDocumentData) {
        if (null == contentData.getTitle() || contentData.getTitle().isEmpty()) {
            return;
        }
        String templatePath = "templates/document/table/table_title.twig";

        //JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", StringEscapeUtils.escapeXml(contentData.getTitle()));
        model.with("uuid", contentData.getBookmarkId());

        TemplateUtils.render(templatePath, wordDocumentData, model);
    }
}
