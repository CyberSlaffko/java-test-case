package ru.ncore.docs.templates.pmi.rel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.rel.schema.ObjectFactory;
import ru.ncore.rel.schema.RelationshipType;
import ru.ncore.rel.schema.RelationshipsType;

import javax.xml.bind.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Вячеслав Молоков on 08.05.2017.
 */
public class RelationManager {
    final static private Logger logger = LoggerFactory.getLogger(RelationManager.class);

    JAXBElement<RelationshipsType> relationships;
    ObjectFactory objectFactory;
    int maxRId = 0;
    private List<Media> mediaList = new ArrayList<>();

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

        Media media = new Media(imgPath, mediaList.size() + 1);

        RelationshipType relationshipType = objectFactory.createRelationshipType();
        relationshipType.setId(nextRId());
        relationshipType.setType(media.getType());
        relationshipType.setTarget(media.getPath());
        relationshipList.add(relationshipType);

        mediaList.add(media);

        return media;
    }

    private String nextRId() {
        maxRId += 1;
        return String.format("rId%d", maxRId);
    }

    public String getRIdForImage(String title) {
        Optional<Media> firstMedia = mediaList.stream().
                filter(rl -> Objects.equals(rl.getImagePath(), title)).findFirst();
        if(!firstMedia.isPresent()) {
            logger.warn(String.format("Requested unknown media: %s", title));
            return "";
        }
        String path = firstMedia.get().getPath();

        Optional<RelationshipType> firstRelation = relationships.getValue().getRelationship().stream().
                filter(rl -> Objects.equals(rl.getTarget(), path)).findFirst();
        if(!firstRelation.isPresent()) {
            logger.warn(String.format("Relation for media %s not found", path));
            return "";
        }

        return firstRelation.get().getId();
    }
}
