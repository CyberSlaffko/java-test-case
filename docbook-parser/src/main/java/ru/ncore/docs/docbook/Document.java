package ru.ncore.docs.docbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.document.DocumentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс представляющий книгу в формате Docbook
 */
public class Document {
    static final Logger logger = LoggerFactory.getLogger(Document.class);
    private DocumentInfo documentInfo = new DocumentInfo();
    private List<ChapterContent> chaptersList = new ArrayList<>();

    private List<ChapterContent> appendicesList = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private Map<String, ChapterContent.Type> links = new HashMap<>();
    private ChapterContent annotation;
    private String absolutePath;

    public Document(String absolutePath) {
        logger.debug(String.format("Path to file: %s", absolutePath));
        this.absolutePath = absolutePath;
    }

    public List<String> getImages() {
        return images;
    }

    /**
     * Статус разбора документа
     *
     * @return true если документ разобран без ошибок, иначе возвращает false
     */
    public boolean isParsedSuccessfully() {
        return true;
    }

    public Type getType() {
        return Type.BOOK;
    }

    public DocumentInfo getInfo() {
        return documentInfo;
    }

    public void setInfo(DocumentInfo info) {
        documentInfo = info;
    }

    public List<ChapterContent> getChaptersList() {
        return chaptersList;
    }

    public ChapterContent getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ChapterContent annotation) {
        this.annotation = annotation;
    }

    public List<ChapterContent> getAppendicesList() {
        return appendicesList;
    }

    public ChapterContent.Type getLinkType(String xref) {
        return links.get(xref);
    }

    public void addLink(String xref, ChapterContent.Type type) {
        links.put(xref, type);
    }

    public void addImage(String xrefLink) {
        images.add(xrefLink);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public enum Type {
        BOOK, ERROR
    }
}
