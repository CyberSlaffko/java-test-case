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

    List<ChapterContent> appendiciesList = new ArrayList<>();

    Map<String, ChapterContent.Type> links = new HashMap<>();

    ChapterContent annotaion;

    public enum Type {
        BOOK, ERROR
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

    public ChapterContent getAnnotaion() {
        return annotaion;
    }

    public void setAnnotaion(ChapterContent annotaion) {
        this.annotaion = annotaion;
    }

    public List<ChapterContent> getAppendiciesList() {
        return appendiciesList;
    }

    public void setAppendiciesList(List<ChapterContent> appendiciesList) {
        this.appendiciesList = appendiciesList;
    }

    public ChapterContent.Type getLinkType(String xref) {
        return links.get(xref);
    }

    public void addLink(String xref, ChapterContent.Type type) {
        links.put(xref, type);
    }
}
