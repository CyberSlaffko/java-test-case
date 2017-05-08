package ru.ncore.docs.templates.pmi.renderers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Рендерер самой картинки.
 *
 * Заголовок и все остальное рендерится в {@link FigureRenderer}
 */
public class ImageRenderer extends IContentRenderer {
    static final Logger logger = LoggerFactory.getLogger(ImageRenderer.class);
    @Override
    public void render(OutputStream wordDocumentData) {
        // rid
        String idForImage = relationManager.getRIdForImage(contentData.getTitle());
        logger.debug(String.format("For image [%s] found rId [%s]", contentData.getTitle(), idForImage));

        String templatePath = "templates/document/image.twig";
        TemplateUtils.render(templatePath, wordDocumentData, new HashMap<String,String>() {{
            put("rid", idForImage);
        }});
    }
}
