package ru.ncore.docs.templates.pmi;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableColumnWidthCalculator {
    static final private Logger logger = LoggerFactory.getLogger(TableColumnWidthCalculator.class);
    static private Pattern PROPORTIONAL_WIDTH = Pattern.compile("^(\\d+(\\.\\d+)?)\\*$");
    static private double MAX_WIDTH_PORTRAIT = 18.0f;

    public List<String> getValues() {
        List<String> accumulator = new ArrayList<>(values.size());

        values.forEach(sizeValue -> accumulator.add(sizeValue.getName()));

        accumulator.sort(Comparator.naturalOrder());
        return accumulator;
    }

    private List<SizeValue> values = new ArrayList<>();
    private double currentWidth = 0.0f;
    private double currentProportions = 0.0f;
    public void addWidth(String colname, String size){
        if (Objects.equals(size, "*")) {
            currentProportions += 1.0f;
            values.add(SizeValue.makeProportional(1.0f, colname));
            return;
        }

        Matcher simpleMatch = PROPORTIONAL_WIDTH.matcher(size);
        if (simpleMatch.find()) {
            double v = Double.parseDouble(simpleMatch.group(1));
            currentProportions += v;
            values.add(SizeValue.makeProportional(v, colname));
            return;
        }

        double v = SizeUtils.textToCm(size);
        currentWidth += v;
        values.add(SizeValue.makeFixed(v, colname));
    }

    public double getWidth() {
        return currentWidth;
    }

    public double getProportions() {
        return currentProportions;
    }

    public double getWidthFor(String colname) {
        Optional<SizeValue> first = values.stream().filter(sv -> Objects.equals(sv.getName(), colname)).findFirst();
        if(!first.isPresent()) {
            logger.error(String.format("Requested width for unknown column %s", colname));
            return 0;
        }

        SizeValue sizeValue = first.get();
        if (!sizeValue.isProportional()) {
            return sizeValue.getWidth();
        }

        return (MAX_WIDTH_PORTRAIT - currentWidth) * sizeValue.getProportionalWidth() / currentProportions;
    }


    private static class SizeValue {
        private double width;
        private double proportionalWidth;
        private String name;

        public double getWidth() {
            return width;
        }

        public double getProportionalWidth() {
            return proportionalWidth;
        }

        public String getName() {
            return name;
        }

        public boolean isProportional() {
            return isProportional;
        }

        boolean isProportional;

        private SizeValue() {
        }

        public static SizeValue makeProportional(double v, String colname) {
            SizeValue sizeValue = new SizeValue();
            sizeValue.isProportional = true;
            sizeValue.name = colname;
            sizeValue.proportionalWidth = v;
            return sizeValue;
        }

        public static SizeValue makeFixed(double v, String colname) {
            SizeValue sizeValue = new SizeValue();
            sizeValue.isProportional = false;
            sizeValue.name = colname;
            sizeValue.width = v;
            return sizeValue;
        }
    }
}
