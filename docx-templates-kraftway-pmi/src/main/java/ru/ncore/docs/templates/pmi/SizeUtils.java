package ru.ncore.docs.templates.pmi;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public abstract class SizeUtils {
    public static long cmToWordPoints(double cm) {
        return Math.round(cm * 72 * 20 / 2.54f);
    }

    public static long cmToWordImagePoints(double cm) {
        return Math.round(cm * 360000);
    }
}
