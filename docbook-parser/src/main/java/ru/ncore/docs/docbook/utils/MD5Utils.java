package ru.ncore.docs.docbook.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public abstract class MD5Utils {
    static String HexForByte(byte b) {
        String Hex = Integer.toHexString((int) b & 0xff);
        boolean hasTwoDigits = (2 == Hex.length());
        if (hasTwoDigits) return Hex;
        else return "0" + Hex;
    }

    static String HexForBytes(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) sb.append(HexForByte(b));
        return sb.toString();
    }

    public static String HexMD5ForString(String text) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] digest = md5.digest(text.getBytes());
        return HexForBytes(digest);
    }
}
