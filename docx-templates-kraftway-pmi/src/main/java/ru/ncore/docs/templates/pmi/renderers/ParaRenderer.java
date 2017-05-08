package ru.ncore.docs.templates.pmi.renderers;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.XREF;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ParaRenderer extends IContentRenderer {
    final static Logger logger = LoggerFactory.getLogger(ParaRenderer.class);
    private String templatePath = "templates/document/para.twig";

    @Override
    public void render(OutputStream wordDocumentData) {
        OutputStream innerData = new ByteArrayOutputStream(1024);
        boolean rendered = innerParaRender(innerData, contentData, templateFor(contentData));

        for(ChapterContent innerContent : contentData.getContentList()) {
            if (innerContent.isList() || innerContent.isTable() || innerContent.isImage()) {
                closePara(wordDocumentData, innerData, rendered);
                innerData = new ByteArrayOutputStream(1024);
                rendered = false;

                IContentRenderer renderer = ContentRendererFactory.getRenderer(innerContent, document, relationManager);
                if (null != renderer) {
                    renderer.render(wordDocumentData);
                }
            }
            else {
                rendered = innerParaRender(innerData, innerContent, templateFor(innerContent)) || rendered;
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
                switch (document.getLinkType(type.getBookmarkId())) {
                    case SECTION:
                    case CHAPTER:
                    case APPENDIX:
                        return "templates/document/ref.twig";
                    default:
                        return "templates/document/ref_obj.twig";
                }
            default:
                logger.warn(String.format("Unknown inline element %s", type.getType()));
        }
        return null;
    }

    private boolean innerParaRender(OutputStream innerData, ChapterContent content, String tmpl) {
        String text = content.getTitle();

        if (XREF == content.getType()) {
            ChapterContent.Type linkType = document.getLinkType(content.getBookmarkId());
            if (linkType != null) {
                switch (linkType) {
                    case CHAPTER:
                        text = "гл. ";
                        break;
                    case APPENDIX:
                        text = "прил. ";
                        break;
                    case SECTION:
                        text = "п. ";
                        break;
                    case TABLE:
                        text = "табл. ";
                        break;
                    case FIGURE:
                        text = "рис. ";
                        break;
                    default:
                        logger.warn(String.format("Unknown XRef type %s", linkType));
                        logger.debug(String.format("XRef linkend=%s, hash=%s", content.getTitle(), content.getBookmarkId()));
                        text = " ";
                        break;
                }
            }
        }

        if (null == text || text.isEmpty()) {
            return false;
        }

        JtwigTemplate innerTemplate = JtwigTemplate.classpathTemplate(tmpl);
        JtwigModel innerModel = JtwigModel.newModel();

        innerModel.with("para", StringEscapeUtils.escapeXml(text));
        innerModel.with("uuid", content.getBookmarkId());

        innerTemplate.render(innerModel, innerData);
        return true;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
