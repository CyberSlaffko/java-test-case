package ru.ncore.docs.templates.pmi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class SizeUtilsTest {

    @Test
    public void cmToWordPointsTest() {
        assertEquals(11906, SizeUtils.cmToWordPoints(21));
        assertEquals(16838, SizeUtils.cmToWordPoints(29.7));
    }

    @Test
    public void textToCmTest() {
        assertEquals(11.1f, SizeUtils.textToCm("111"), 0.001f);
        assertEquals(11.11f, SizeUtils.textToCm("111.1"), 0.001f);
        assertEquals(11.1f, SizeUtils.textToCm("111mm"), 0.001f);
        assertEquals(11.12f, SizeUtils.textToCm("111.2mm"), 0.001f);
        assertEquals(11.1f, SizeUtils.textToCm("11.1cm"), 0.001f);
        assertEquals(11.1f, SizeUtils.textToCm("11,1cm"), 0.001f);
        assertEquals(1.0f, SizeUtils.textToCm("1cm"), 0.001f);
        assertEquals(1.0f, SizeUtils.textToCm("1 cm"), 0.001f);
    }
}
