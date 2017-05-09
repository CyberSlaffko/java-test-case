package ru.ncore.docs.templates.pmi.renderers.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.*;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableMatrix {
    static final private Logger logger = LoggerFactory.getLogger(TableMatrix.class);
    private int rows;

    public int getColumns() {
        return columns;
    }

    private int columns;
    private List<ChapterContent> colspecs;

    public TableMatrixCell get(int row, int column) {
        return matrix[row][column];
    }

    public int getRows() {
        return rows;
    }

    public enum CellAction {
        MERGE_RESTART, MERGE,
        SIMPLE,
        GRID_SPAN, GRID_SPAN_SKIP
    }

    private TableMatrixCell[][] matrix;


    private TableMatrix() {}

    public static TableMatrix generate(int rows, int columns, List<ChapterContent> names) {
        TableMatrix tableMatrix = new TableMatrix();
        tableMatrix.matrix = new TableMatrixCell[rows][columns];
        tableMatrix.rows = rows;
        tableMatrix.columns = columns;
        tableMatrix.colspecs = names;
        return tableMatrix;
    }

    public void addCellToRow(int row, ChapterContent chapterContent) {
        int freeCellIndex = getFreeCellIndex(row);
        if (-1 == freeCellIndex) {
            logger.error("Cannot add more cell into row!");
            return;
        }

        String colname = String.valueOf(freeCellIndex + 1);

        if(isRowSpan(chapterContent)) {
            int spanSize =  Integer.parseInt(chapterContent.getAdditionalAttributes().get("morerows"));
            matrix[row][freeCellIndex] = new TableMatrixCell(colname, CellAction.MERGE_RESTART, chapterContent.getContentList());
            for(int i = row + 1; i <= row + spanSize; i++) {
                matrix[i][freeCellIndex] = new TableMatrixCell(colname, CellAction.MERGE);
            }
        } else if(isCellSpan(chapterContent)) {
            int spanSize =  getCellSpanSize(chapterContent);
            if (spanSize + freeCellIndex > columns) {
                logger.error(String.format("Table has %d columns, but requested %d columns", columns, freeCellIndex + spanSize + 1));
                return;
            }
            matrix[row][freeCellIndex] = new TableMatrixCell(colname, CellAction.GRID_SPAN, chapterContent.getContentList(), spanSize);
            for (int i = freeCellIndex + 1; i < freeCellIndex + spanSize; i++) {
                matrix[row][i] = new TableMatrixCell(colname, CellAction.GRID_SPAN_SKIP, spanSize);
            }

        } else {
            matrix[row][freeCellIndex] = new TableMatrixCell(colname, CellAction.SIMPLE, chapterContent.getContentList());
        }

    }

//    private String colNameByPosition(int freeCellIndex) {
//        String colnum = String.valueOf(freeCellIndex + 1);
//
//        Optional<ChapterContent> first = colspecs.stream().filter(chapterContent -> {
//            return Objects.equals(chapterContent.getAdditionalAttributes().get("colnum"), colnum);
//        }).findFirst();
//        if (!first.isPresent()) {
//            return null;
//        }
//
//        return first.get().getTitle();
//    }

    private int getCellSpanSize(ChapterContent chapterContent) {
        String namest = chapterContent.getAdditionalAttributes().get("namest");
        String nameend = chapterContent.getAdditionalAttributes().get("nameend");

        int spanSize = nameToColIndex(nameend) - nameToColIndex(namest);
        logger.debug(String.format("Span size: %d", spanSize + 1));
        return spanSize + 1;
    }

    private int nameToColIndex(String colspecName) {
        Optional<ChapterContent> first = colspecs.stream().filter(chapterContent -> Objects.equals(chapterContent.getTitle(), colspecName)).findFirst();
        if (!first.isPresent()) {
            return -1;
        }

        return Integer.parseInt(first.get().getAdditionalAttributes().get("colnum"));
    }

    private boolean isCellSpan(ChapterContent chapterContent) {
        String namest = chapterContent.getAdditionalAttributes().get("namest");
        String nameend = chapterContent.getAdditionalAttributes().get("nameend");

        if (null == namest || null == nameend) {
            return false;
        }

        return true;
    }

    private boolean isRowSpan(ChapterContent chapterContent) {
        String morerows = chapterContent.getAdditionalAttributes().get("morerows");
        if (null == morerows) {
            return false;
        }

        return true;
    }

    private int getFreeCellIndex(int row) {
        for (int i = 0; i < columns; i++) {
            if (matrix[row][i] == null) {
                return i;
            }
        }

        return -1;
    }
}
