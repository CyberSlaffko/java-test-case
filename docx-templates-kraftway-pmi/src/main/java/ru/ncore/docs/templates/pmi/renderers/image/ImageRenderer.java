package ru.ncore.docs.templates.pmi.renderers.image;

import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.SizeUtils;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.awt.*;
import java.io.OutputStream;

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
        logger.debug(String.format("Original width/height: %d / %d", originalWidth, originalHeight));

        double requestedWidth = 0.0;
        double requestedHeight = 0.0;
        if (isInMediaobject()) {
            //пересчет размеров под указанный в docbook, либо на максимальную ширину
            requestedWidth = SizeUtils.textToCm(getWidth());
            requestedHeight = (originalHeight * requestedWidth) / originalWidth;
        } else {
            //пересчет размеров под 100%, но не больше указанного в docbook, либо максимальной ширины

            int xDpi = 96, yDpi = 96;
            try {
                xDpi = yDpi = Toolkit.getDefaultToolkit().getScreenResolution();
            } catch (Exception awte) {}

            double cmPerInch = 2.54;
            double maxWidthCm = SizeUtils.textToCm(getWidth());
            requestedWidth = ((double) originalWidth / xDpi * cmPerInch);
            requestedHeight = ((double) originalHeight / yDpi * cmPerInch);
            double maxWidth = SizeUtils.cmToWordImagePoints(maxWidthCm);
            if (requestedWidth > maxWidth)
            {
                double ratio = requestedHeight / requestedWidth;
                requestedWidth = maxWidth;
                requestedHeight = requestedWidth * ratio;
            }
        }
        logger.debug(String.format("Requested width/height: %f / %f", requestedWidth, requestedHeight));

        String templatePath = "templates/document/image.twig";
        TemplateUtils.render(templatePath, wordDocumentData, JtwigModel.newModel()
            .with("rid", idForImage)
            .with("width", String.valueOf(SizeUtils.cmToWordImagePoints(requestedWidth)))
            .with("height", String.valueOf(SizeUtils.cmToWordImagePoints(requestedHeight)))
        );
    }

    public String getWidth() {
        return contentData.getAdditionalAttributes().getOrDefault("width", "18cm");
    }

    public boolean isInMediaobject() {
        return contentData.getAdditionalAttributes().getOrDefault("isInMediaobject", "0").equals("1");
    }
}
