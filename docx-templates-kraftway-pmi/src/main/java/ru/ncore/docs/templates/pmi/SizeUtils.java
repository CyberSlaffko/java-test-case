package ru.ncore.docs.templates.pmi;

import com.sun.glass.ui.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public abstract class SizeUtils {
    private static final Logger logger = LoggerFactory.getLogger(Size.class);

    private static Pattern SIZE_SIMPLE = Pattern.compile("^(\\d+(\\.\\d+)?)$");
    private static Pattern SIZE_CM = Pattern.compile("^(\\d+(\\.\\d+)?)cm$");
    private static Pattern SIZE_MM = Pattern.compile("^(\\d+(\\.\\d+)?)mm$");

    public static long cmToWordPoints(double cm) {
        return Math.round(cm * 72 * 20 / 2.54f);
    }

    public static long cmToWordImagePoints(double cm) {
        return Math.round(cm * 360000);
    }

    public static double textToCm(String s) {
        Matcher simpleMatch = SIZE_SIMPLE.matcher(s);
        DecimalFormat f = new DecimalFormat("#.##");
        if (simpleMatch.find()) {
            return Double.parseDouble(simpleMatch.group(1)) / 10;
        }

        Matcher cmMatch = SIZE_CM.matcher(s);
        if (cmMatch.find()) {
            return Double.parseDouble(cmMatch.group(1));
        }

        Matcher mmMatch = SIZE_MM.matcher(s);
        if (mmMatch.find()) {
            return Double.parseDouble(mmMatch.group(1)) / 10;
        }

        logger.error(String.format("Unknown size dimensions: %s", s));
        return 0.0f;
    }
}
