package ru.ncore.docs.templates.pmi.renderers;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class FigureRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }

        String templatePath = "templates/document/figure_title.twig";
        TemplateUtils.render(templatePath, wordDocumentData, new HashMap<String,String>() {{
            put("title", contentData.getTitle());
            put("uuid", contentData.getBookmarkId());
        }});
    }
}
