package ru.ncore.docs.docbook.document;

import java.util.*;

/**
 * Содержимое документа (кроме
 */
public class ChapterContent {
    private final String uuid;
    private String bookmarkId;
    private String title;
    private int level;
    private Type type;
    private ChapterType chapterType;

    public Map<String, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    private Map<String, String> additionalAttributes = new HashMap<>();
    private List<ChapterContent> contentList = new ArrayList<>();
    public ChapterContent() {
        uuid = String.valueOf(UUID.randomUUID().hashCode());
    }

    /**
     * В зависимости от результата getType()
     *   - IMAGEDATA - это путь до картинки в Docbook
     *   - TGROUP - это количество столбцов
     * @return Заголовок или значение специального поля
     */
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
        if (null == bookmarkId || bookmarkId.isEmpty()) {
            return uuid;
        }
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public boolean isList() {
        return type == Type.ITEMLIST || type == Type.ORDEREDLIST;
    }

    public boolean isTable() {
        return type == Type.TABLE;
    }

    public ChapterType getChapterType() {
        return chapterType;
    }

    public void setChapterType(ChapterType chapterType) {
        this.chapterType = chapterType;
    }

    public boolean isImage() {
        return type == Type.MEDIAOBJECT || type == Type.FIGURE;
    }

    /**
     * Позволяет добавлять дополнительные аттрибуты к содержимому
     * @param attr Название аттрибута
     * @param value Значение аттрибута
     */
    public void addAdditionalAttribute(String attr, String value) {
        additionalAttributes.put(attr, value);
    }

    /**
     * Типы содержимого документа
     */
    public enum Type {
        ANNOTATION, CHAPTER, APPENDIX,
        /**
         * Раздел внутри Аннотации, Главы или Приложения
         */
        SECTION,

        /**
         * Параграф
         */
        PARA,

        // Списки
        ITEMLIST, // Ненумерованный список
        ITEMLIST_ITEM, // Элемент ненумерованного списка
        ORDEREDLIST, // Нумерованный список
        ORDEREDLIST_ITEM, // Элемент нумерованного списка


        PHRASE, // Фраза (обычно не разрываемая на несколько строк)
        TEXT, // Текст в абзаце или ячееке таблицы
        XREF, // Ссылка на другой

        // Таблица
        TABLE, // Таблица


        PROGRAMLISTING,

        FIGURE,
        MEDIAOBJECT,
        IMAGEOBJECT,
        IMAGEDATA,

        TABLE_INFO, TABLE_HEAD, TABLE_ROW, TABLE_CELL,

        TGROUP,
        TABLE_BODY, TABLE_COLSPEC
    }

    public enum ChapterType {
        ANNOTATION, CHAPTER, APPENDIX, TABLE, TABLE_BODY, TABLE_HEAD
    }
}
