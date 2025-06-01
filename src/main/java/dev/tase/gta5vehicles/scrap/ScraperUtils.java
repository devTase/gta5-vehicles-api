package dev.tase.gta5vehicles.scrap;

import org.jsoup.nodes.Element;

import java.math.BigDecimal;

public class ScraperUtils {
    public static String getCleanText(Element parent, int rowIndex, int colIndex) {
        if (parent == null || parent.childrenSize() <= rowIndex) return null;

        Element row = parent.child(rowIndex);
        if (row.childrenSize() <= colIndex) return null;

        String text = row.child(colIndex).text();
        if (text.isBlank() || text.equalsIgnoreCase("N/A")) return null;

        return text;
    }

    public static boolean isDouble(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Double getDouble(Element parent, int rowIndex, int colIndex) {
        String value = getCleanText(parent, rowIndex, colIndex);
        if (value == null || value.equals("N/A")) return 0.0;

        value = value.replace(",", "").replaceAll("[^\\d.\\-]", "");

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer getInteger(Element parent, int rowIndex, int colIndex) {
        String value = getCleanText(parent, rowIndex, colIndex);
        if (value == null) return null;

        value = value.replaceAll("\\D", "");
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal getBigDecimal(Element parent, int rowIndex, int colIndex) {
        String value = getCleanText(parent, rowIndex, colIndex);
        if (value == null) return new BigDecimal("0.0");

        value = value.replaceAll("[^\\d.]", "");
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
