package ru.ncore.docs.docbook.document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterContent {
    private final String uuid;

    public ChapterContent() {
        uuid = UUID.randomUUID().toString();
    }

    public enum Type {
        SECTION, PARA, ITEMLIST, ITEMLIST_ITEM, ORDEREDLIST, ORDEREDLIST_ITEM
    }

    private String title;

    private int level;

    private Type type;

    private List<ChapterContent> contentList = new ArrayList<>();

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
}
