package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ParaRenderer implements IContentRenderer {
    private ChapterContent contentData;

    @Override
    public void render(OutputStream wordDocumentData) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/document/para.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("para", contentData.getTitle());
        model.with("uuid", contentData.getUuid());

        template.render(model, wordDocumentData);
    }

    @Override
    public IContentRenderer setContent(ChapterContent contentData) {
        this.contentData = contentData;
        return this;
    }
}
