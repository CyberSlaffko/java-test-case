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
}
