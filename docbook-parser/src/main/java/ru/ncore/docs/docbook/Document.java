package ru.ncore.docs.docbook;

import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Document {

    DocumentInfo documentInfo = new DocumentInfo();
    List<Chapter> chaptersList = new ArrayList<>();

    Chapter annotaion;

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

    public List<Chapter> getChaptersList() {
        return chaptersList;
    }

    public Chapter getAnnotaion() {
        return annotaion;
    }

    public void setAnnotaion(Chapter annotaion) {
        this.annotaion = annotaion;
    }

}
