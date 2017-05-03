package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class SectionRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        String templatePath = String.format("templates/document/section_%d.twig", contentData.getLevel());

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", contentData.getTitle());
        model.with("uuid", contentData.getUuid());

        template.render(model, wordDocumentData);

        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }
}
