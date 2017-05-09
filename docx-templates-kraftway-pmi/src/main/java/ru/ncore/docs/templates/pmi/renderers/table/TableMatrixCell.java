package ru.ncore.docs.templates.pmi.renderers.table;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableMatrixCell {

    public TableMatrixCell(String colName, TableMatrix.CellAction cellAction, int spanSize) {
        this.colName = colName;
        this.cellType = cellAction;
        this.spanSize = spanSize;
    }

    public TableMatrixCell(String colName, TableMatrix.CellAction cellType, List<ChapterContent> contentList, int spanSize) {
        this.colName = colName;
        this.cellType = cellType;
        this.contentList = contentList;
        this.spanSize = spanSize;
    }

    public String getColName() {
        return colName;
    }

    private final String colName;
    private final int spanSize;
    private final TableMatrix.CellAction cellType;
    private List<ChapterContent> contentList = new ArrayList<>();

    public TableMatrixCell(String colName, TableMatrix.CellAction cellType, List<ChapterContent> contentList) {
        this.colName = colName;
        this.cellType = cellType;
        this.contentList = contentList;
        spanSize = 0;
    }

    public List<ChapterContent> getContent() {
        return contentList;
    }

    public TableMatrixCell(String colName, TableMatrix.CellAction cellType) {
        this.colName = colName;
        this.cellType = cellType;
        spanSize = 0;
    }

    public TableMatrix.CellAction getCellType() {
        return cellType;
    }

    public int getSpanSize() {
        return spanSize;
    }
}
