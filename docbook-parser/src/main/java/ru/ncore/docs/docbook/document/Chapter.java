package ru.ncore.docs.docbook.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class Chapter {
    private ChapterContent content;

    public Chapter() {
        content = new ChapterContent();
        content.setLevel(1);
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
}
