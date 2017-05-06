package ru.ncore.docs.docbook;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.document.DocumentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Document {

    DocumentInfo documentInfo = new DocumentInfo();
    List<ChapterContent> chaptersList = new ArrayList<>();

    List<ChapterContent> appendicesList = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private Map<String, ChapterContent.Type> links = new HashMap<>();
    private ChapterContent annotation;

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

    public void setAppendicesList(List<ChapterContent> appendicesList) {
        this.appendicesList = appendicesList;
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

    public enum Type {
        BOOK, ERROR
    }
}
