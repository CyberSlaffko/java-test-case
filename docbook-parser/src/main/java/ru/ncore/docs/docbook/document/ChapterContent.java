package ru.ncore.docs.docbook.document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterContent {
    public enum Type {
        SECTION, PARA, ITEMLIST, ITEMLIST_ITEM, ORDEREDLIST, ORDEREDLIST_ITEM, PHRASE, TEXT, XREF
    }

    private final String uuid;
    private String bookmarkId;
    private String title;
    private int level;
    private Type type;
    private List<ChapterContent> contentList = new ArrayList<>();

    public ChapterContent() {
        uuid = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChapterContent> getContentList() {
        return contentList;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public boolean isList() {
        return type == Type.ITEMLIST || type == Type.ORDEREDLIST;
    }
}
