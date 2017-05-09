package ru.ncore.docs.templates.pmi;

import org.junit.jupiter.api.Test;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.renderers.table.TableMatrix;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
class TableMatrixTest {
    @Test
    void fillMatrix() {
        List<ChapterContent> colspecs = new ArrayList<>();
        ChapterContent colspec1 = new ChapterContent();
        colspec1.setTitle("col1");
        colspec1.addAdditionalAttribute("colnum", "1");
        colspecs.add(colspec1);
        ChapterContent colspec2 = new ChapterContent();
        colspec2.setTitle("col2");
        colspec2.addAdditionalAttribute("colnum", "2");
        colspecs.add(colspec2);
        ChapterContent colspec3 = new ChapterContent();
        colspec3.setTitle("col3");
        colspec3.addAdditionalAttribute("colnum", "3");
        colspecs.add(colspec3);
        ChapterContent colspec4 = new ChapterContent();
        colspec4.setTitle("col4");
        colspec4.addAdditionalAttribute("colnum", "4");
        colspecs.add(colspec4);
        ChapterContent colspec5 = new ChapterContent();
        colspec5.setTitle("col5");
        colspec5.addAdditionalAttribute("colnum", "5");
        colspecs.add(colspec5);
        ChapterContent colspec6 = new ChapterContent();
        colspec6.setTitle("col6");
        colspec6.addAdditionalAttribute("colnum", "6");
        colspecs.add(colspec6);
        ChapterContent colspec7 = new ChapterContent();
        colspec7.setTitle("col7");
        colspec7.addAdditionalAttribute("colnum", "7");
        colspecs.add(colspec7);

        TableMatrix matrix = TableMatrix.generate(2, 7, colspecs);

        ChapterContent cell1_1 = new ChapterContent();
        cell1_1.addAdditionalAttribute("morerows", "1");
        matrix.addCellToRow(0, cell1_1);

        ChapterContent cell1_2 = new ChapterContent();
        cell1_2.addAdditionalAttribute("namest", "col2");
        cell1_2.addAdditionalAttribute("nameend", "col4");
        matrix.addCellToRow(0, cell1_2);

        ChapterContent cell1_3 = new ChapterContent();
        cell1_3.addAdditionalAttribute("morerows", "1");
        matrix.addCellToRow(0, cell1_3);

        ChapterContent cell1_4 = new ChapterContent();
        cell1_4.addAdditionalAttribute("morerows", "1");
        matrix.addCellToRow(0, cell1_4);

        ChapterContent cell1_5 = new ChapterContent();
        cell1_5.addAdditionalAttribute("morerows", "1");
        matrix.addCellToRow(0, cell1_5);

        ChapterContent cell2_1 = new ChapterContent();
        matrix.addCellToRow(1, cell2_1);

        ChapterContent cell2_2 = new ChapterContent();
        matrix.addCellToRow(1, cell2_2);

        ChapterContent cell2_3 = new ChapterContent();
        matrix.addCellToRow(1, cell2_3);

        assertAll(
                () -> assertEquals(TableMatrix.CellAction.MERGE_RESTART, matrix.get(0,0).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.GRID_SPAN, matrix.get(0,1).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.GRID_SPAN_SKIP, matrix.get(0,2).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.GRID_SPAN_SKIP, matrix.get(0,3).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE_RESTART, matrix.get(0,4).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE_RESTART, matrix.get(0,5).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE_RESTART, matrix.get(0,6).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE, matrix.get(1,0).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.SIMPLE, matrix.get(1,1).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.SIMPLE, matrix.get(1,2).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.SIMPLE, matrix.get(1,3).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE, matrix.get(1,4).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE, matrix.get(1,5).getCellType()),
                () -> assertEquals(TableMatrix.CellAction.MERGE, matrix.get(1,6).getCellType())
        );
    }
}