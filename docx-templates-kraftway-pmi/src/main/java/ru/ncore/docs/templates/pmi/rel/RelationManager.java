package ru.ncore.docs.templates.pmi.rel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.rel.schema.ObjectFactory;
import ru.ncore.rel.schema.RelationshipType;
import ru.ncore.rel.schema.RelationshipsType;

import javax.imageio.ImageIO;
import javax.xml.bind.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 08.05.2017.
 */
public class RelationManager {
    final static private Logger logger = LoggerFactory.getLogger(RelationManager.class);

    JAXBElement<RelationshipsType> relationships;
    ObjectFactory objectFactory;
    int maxRId = 0;

    public RelationManager() {
        objectFactory = new ObjectFactory();
        relationships = objectFactory.createRelationships(objectFactory.createRelationshipsType());
    }

    public void parseRelFile(InputStream relationFileStream) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            relationships = (JAXBElement<RelationshipsType>)unmarshaller.unmarshal(relationFileStream);

            findMaximumRId();
        } catch (JAXBException e) {
            logger.error("Unmarshaling error", e);
        }
    }

    private void findMaximumRId() {
        List<RelationshipType> relationshipList = relationships.getValue().getRelationship();

        for(RelationshipType rt : relationshipList) {
            String rtId = rt.getId();
            int proposalMaxRId = Integer.parseInt(rtId.replaceAll("[\\D]", ""));

            if(proposalMaxRId > maxRId) {
                maxRId = proposalMaxRId;
            }
        }
        logger.debug(String.format("Found maximum rId = %d", maxRId));
    }

    public ByteArrayOutputStream generateRelFile() {
        JAXBContext jaxbContext = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        try {

            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(relationships, outputStream);
        } catch (JAXBException e) {
            logger.error("Marshaling error", e);
        }
        return outputStream;
    }

    public Media addRelation(String imgPath) {
        List<RelationshipType> relationshipList = relationships.getValue().getRelationship();

        Media media = new Media(imgPath);

        RelationshipType relationshipType = objectFactory.createRelationshipType();
        relationshipType.setId(nextRId());
        relationshipType.setType(media.getType());
        relationshipType.setTarget(media.getPath());
        relationshipList.add(relationshipType);

        return media;
    }

    private String nextRId() {
        maxRId += 1;
        return String.format("rId%d", maxRId);
    }

    public static class Media {
        final static private Logger logger = LoggerFactory.getLogger(Media.class);
        private final String mediaPath;
        private String imgPath;

        public Media(String imgPath) {
            this.imgPath = imgPath;
            mediaPath = String.format("/word/media/image_%d.png", imgPath.hashCode());
        }

        public String getType() {
            return "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
        }

        public ByteArrayOutputStream toPNG() {
            logger.debug(String.format("Processing image %s", imgPath));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10240);;
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
                ImageIO.write(bufferedImage, "png", outputStream);
            } catch (IOException e) {
                logger.error(String.format("Cannot read image file %s", imgPath), e);
            }

            return outputStream;
        }

        public String getPath() {
            return mediaPath;
        }
    }
}
