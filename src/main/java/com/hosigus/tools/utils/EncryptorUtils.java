package com.hosigus.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 某只机智 on 2018/5/5.
 * MD5加密
 */

public class EncryptorUtils {
    private EncryptorUtils() {throw new UnsupportedOperationException("cannot be instantiated");}

    public static String MD5Encode(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bys = digest.digest(code.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bys) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    builder.append("0");
                }
                builder.append(str);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}