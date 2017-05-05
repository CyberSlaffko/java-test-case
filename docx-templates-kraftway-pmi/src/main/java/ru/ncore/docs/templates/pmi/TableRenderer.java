package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class TableRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        String templatePath = "templates/document/table_title.twig";

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", contentData.getTitle());
        model.with("uuid", contentData.getBookmarkId());

        template.render(model, wordDocumentData);

        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }
}
