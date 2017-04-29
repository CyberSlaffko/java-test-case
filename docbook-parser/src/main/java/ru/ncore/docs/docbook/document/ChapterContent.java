package ru.ncore.docs.docbook.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterContent {
    private String title;

    int level;

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
}
