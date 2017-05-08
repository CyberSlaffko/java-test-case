package ru.ncore.docs.templates.pmi.rel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Вячеслав Молоков on 08.05.2017.
 */
public class Media {
    final static private Logger logger = LoggerFactory.getLogger(Media.class);
    private final String mediaPath;
    private String imgPath;
    private int width;
    private int height;

    public Media(String imgPath, int mediaIndex) {
        this.imgPath = imgPath;
        mediaPath = String.format("media/image%d.png", mediaIndex);
    }

    public String getType() {
        return "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
    }

    public ByteArrayOutputStream toPNG() {
        logger.debug(String.format("Processing image %s", imgPath));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10240);
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
            height = bufferedImage.getHeight();
            width = bufferedImage.getWidth();
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            logger.error(String.format("Cannot read image file %s", imgPath), e);
        }

        return outputStream;
    }

    public String getPath() {
        return mediaPath;
    }

    public String getImagePath() {
        return imgPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
