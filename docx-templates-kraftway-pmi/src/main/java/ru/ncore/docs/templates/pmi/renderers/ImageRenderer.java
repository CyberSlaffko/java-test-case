package ru.ncore.docs.templates.pmi.renderers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.SizeUtils;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Optional;

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
        int originalWidth = relationManager.getImageWeight(contentData.getTitle());
        int originalHeight = relationManager.getImageHeight(contentData.getTitle());
        logger.debug(String.format("For image [%s] found rId [%s]", contentData.getTitle(), idForImage));
        logger.debug(String.format("Original weight/height: %d / %d", originalWidth, originalHeight));

        double requestedWidth = SizeUtils.textToCm(getWidth());
        double requestedHeight = (originalHeight * requestedWidth) / originalWidth;
        logger.debug(String.format("Requested weight/height: %f / %f", requestedWidth, requestedHeight));

        String templatePath = "templates/document/image.twig";
        TemplateUtils.render(templatePath, wordDocumentData, new HashMap<String,String>() {{
            put("rid", idForImage);
            put("width", String.valueOf(SizeUtils.cmToWordImagePoints(requestedWidth)));
            put("height", String.valueOf(SizeUtils.cmToWordImagePoints(requestedHeight)));
        }});
    }

    public String getWidth() {
        Optional<ChapterContent> first = contentData.getContentList().stream().filter(cd -> cd.getType().equals(ChapterContent.Type.WIDTH)).findFirst();

        if (!first.isPresent()) {
            return "18cm";
        }

        return first.get().getTitle();
    }
}
