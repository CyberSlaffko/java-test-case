package ru.ncore.docs.templates.pmi;

import org.junit.jupiter.api.Test;
import ru.ncore.docs.templates.pmi.renderers.table.TableColumnWidthCalculator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
class TableColumnWidthCalculatorTest {
    @Test
    void addWidth() {
        TableColumnWidthCalculator tableColumnWidthCalculator = new TableColumnWidthCalculator();
        tableColumnWidthCalculator.addWidth("1", "1*");
        tableColumnWidthCalculator.addWidth("2", "1cm");
        assertAll(
                () -> assertEquals(1.0f, tableColumnWidthCalculator.getWidth(), 0.01f),
                () -> assertEquals(1.0f, tableColumnWidthCalculator.getProportions(), 0.01f)
        );

        tableColumnWidthCalculator.addWidth("3", "*");
        assertEquals( 2.0f, tableColumnWidthCalculator.getProportions(), 0.01f);

        tableColumnWidthCalculator.addWidth("4", "11mm");
        assertEquals(2.1f, tableColumnWidthCalculator.getWidth(), 0.01f);
    }

    @Test
    void calcSizes() {
        TableColumnWidthCalculator tableColumnWidthCalculator = new TableColumnWidthCalculator();
        tableColumnWidthCalculator.addWidth("1", "1*");
        tableColumnWidthCalculator.addWidth("2", "1cm");
        tableColumnWidthCalculator.addWidth("3", "2*");
        tableColumnWidthCalculator.addWidth("4", "8cm");

        assertAll(
            () -> assertEquals(3.0f, tableColumnWidthCalculator.getWidthFor("1"), 0.01f),
            () -> assertEquals(1.0f, tableColumnWidthCalculator.getWidthFor("2"), 0.01f),
            () -> assertEquals(6.0f, tableColumnWidthCalculator.getWidthFor("3"), 0.01f),
            () -> assertEquals(8.0f, tableColumnWidthCalculator.getWidthFor("4"), 0.01f)
        );
    }

}