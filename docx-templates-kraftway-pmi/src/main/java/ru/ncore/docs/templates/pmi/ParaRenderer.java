package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ParaRenderer extends IContentRenderer {
    private String templatePath = "templates/document/para.twig";

    @Override
    public void render(OutputStream wordDocumentData) {
        OutputStream innerData = new ByteArrayOutputStream(1024);
        boolean rendered = innerParaRender(innerData, contentData.getTitle(), contentData.getUuid(), templateFor(contentData));

        for(ChapterContent innerContent : contentData.getContentList()) {
            if (innerContent.isList() || innerContent.isTable()) {
                closePara(wordDocumentData, innerData, rendered);
                innerData = new ByteArrayOutputStream(1024);
                rendered = false;

                IContentRenderer renderer = ContentRendererFactory.getRenderer(innerContent, document);
                if (null != renderer) {
                    renderer.render(wordDocumentData);
                }
            }
            else {
                rendered = innerParaRender(innerData, innerContent.getTitle(), innerContent.getUuid(), templateFor(innerContent)) || rendered;
            }
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

    private String templateFor(ChapterContent type) {
        switch (type.getType()) {
            case PARA:
            case PHRASE:
            case TEXT:
                return "templates/document/para_r.twig";
            case XREF:
                if (document.getLinkType(type.getTitle()) == ChapterContent.Type.SECTION) {
                    return "templates/document/ref.twig";
                } else {
                    return "templates/document/ref_obj.twig";
                }
            default:
                System.out.printf("[W004] unknown inpara %s", type.getType());
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
}
