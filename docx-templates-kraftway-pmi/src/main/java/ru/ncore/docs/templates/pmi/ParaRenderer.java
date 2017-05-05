package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ParaRenderer extends IContentRenderer {
    private String templatePath = "templates/document/para.twig";

    @Override
    public void render(OutputStream wordDocumentData) {
        OutputStream innerData = new ByteArrayOutputStream(1024);
        boolean rendered = innerParaRender(innerData, contentData.getTitle(), contentData.getUuid(), templateFor(contentData.getType()));

        for(ChapterContent innerContent : contentData.getContentList()) {
            if (innerContent.isList()) {
                closePara(wordDocumentData, innerData, rendered);
                innerData = new ByteArrayOutputStream(1024);
                rendered = false;

                IContentRenderer renderer = ContentRendererFactory.getRenderer(innerContent);
                if (null != renderer) {
                    renderer.render(wordDocumentData);
                }
            }

            rendered = innerParaRender(innerData, innerContent.getTitle(), innerContent.getUuid(), templateFor(innerContent.getType())) || rendered;
        }

        closePara(wordDocumentData, innerData, rendered);
    }

    private void closePara(OutputStream wordDocumentData, OutputStream innerData, boolean rendered) {
        if (rendered) {
            JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
            JtwigModel model = JtwigModel.newModel();
            model.with("body", innerData);

            template.render(model, wordDocumentData);
        }
    }

    private String templateFor(ChapterContent.Type type) {
        switch (type) {
            case PARA:
            case PHRASE:
            case TEXT:
                return "templates/document/para_r.twig";
            case XREF:
                return "templates/document/ref.twig";
        }
        return null;
    }

    private boolean innerParaRender(OutputStream innerData, String text, String uuid, String tmpl) {
        if (null == text || text.isEmpty()) {
            return false;
        }

        JtwigTemplate innerTemplate = JtwigTemplate.classpathTemplate(tmpl);
        JtwigModel innerModel = JtwigModel.newModel();

        innerModel.with("para", text);
        innerModel.with("uuid", uuid);

        innerTemplate.render(innerModel, innerData);
        return true;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    private boolean isListOnly() {
        boolean emptyPara = contentData.getTitle() == null || contentData.getTitle().isEmpty();
        boolean firstChildList = contentData.getContentList().size() > 0 && contentData.getContentList().get(0).isList();
        return emptyPara && firstChildList;
    }
}
