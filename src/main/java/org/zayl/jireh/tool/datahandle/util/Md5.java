package org.zayl.jireh.tool.datahandle.util;

import java.security.MessageDigest;

/**
 * @author last_
 */
public class Md5 {

    public static final char[] CHS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * MD5加密
     * @param str
     * @return 32位16进制字符串, 无符号
     * @throws Exception
     */
    public static String getMD5(String str) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("UTF-8"));
        byte[] bys = md5.digest();

        StringBuilder sb = new StringBuilder(32);
        int index;
        for (byte b : bys) {
            // 将Java 中带符号位的byte 转换为不带符号位的
            index = b & 0xff;
            // 16进制数的高位
            sb.append(CHS[index >> 4]);
            // 16进制数的低位
            sb.append(CHS[index % 16]);
        }
        // 返回的结果为32位的16进制数字符串
        return sb.toString();
    }
}
