package ru.ncore.docs.docbook.document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Chapter {
    private final String uuid;
    private ChapterContent content;

    public Chapter() {
        content = new ChapterContent();
        content.setLevel(1);
        uuid = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return content.getTitle();
    }

    public void setTitle(String title) {
        content.setTitle(title);
    }

    public List<ChapterContent> getContentList() {
        return content.getContentList();
    }

    public int getLevel() {
        return content.getLevel();
    }

    public String getUuid() {
        return uuid;
    }

    public void setBookmarkId(String bookmarkId) {
        content.setBookmarkId(bookmarkId);
    }

    public String getBookmarkId() {
        return content.getBookmarkId();
    }
}
