package ru.ncore.docs.templates.pmi.renderers;

import org.jtwig.JtwigModel;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by VKolesnikov on 30.09.2017.
 */
public class MediaObjectRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        ByteArrayOutputStream innerData = new ByteArrayOutputStream(1024);

        for (ChapterContent innerContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(innerContent, document, relationManager);
            if (null != renderer) {
                renderer.render(innerData);
            }
        }

        JtwigModel model = JtwigModel.newModel();
        model.with("body", new String(innerData.toByteArray(), TemplateUtils.getCfg().getResourceConfiguration().getDefaultCharset()));

        String templatePath = "templates/document/mediaobject.twig";

        TemplateUtils.render(templatePath, wordDocumentData, model);
    }
}
