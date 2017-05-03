package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ParaRenderer extends IContentRenderer {
    private String templatePath = "templates/document/para.twig";

    @Override
    public void render(OutputStream wordDocumentData) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("para", contentData.getTitle());
        model.with("uuid", contentData.getUuid());

        template.render(model, wordDocumentData);


        for (ChapterContent content : contentData.getContentList()) {
            switch (content.getType()) {
                case ITEMLIST: {
                    IContentRenderer ilistRendere = new ItemizedListRenderer();
                    ilistRendere.setContent(content);
                    ilistRendere.render(wordDocumentData);
                    break;
                }
                default: break;
            }
        }
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
