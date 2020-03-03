package org.olexec.execute;

/**
 * @author ycw
 */
public class ByteUtils {

    public static int byte2Int(byte[] b, int start, int len) {
        int res = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int cur = ((int) b[i]) & 0xff;
            cur <<= (--len) * 8;
            res += cur;
        }
        return 0;
    }

    public static byte[] int2Byte(int num, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((num >> (8 * i)) & 0xff);
        }
        return b;
    }

    public static String byte2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }

    public static byte[] string2Byte(String string) {
        return string.getBytes();
    }

    public static byte[] byteReplaces(byte[] oldBytes, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[oldBytes.length - len + replaceBytes.length];
        System.arraycopy(oldBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, offset, newBytes, 0, replaceBytes.length);
        System.arraycopy(oldBytes, offset + len, newBytes, offset + replaceBytes.length, oldBytes.length - offset - len);
        return newBytes;
    }


    public static void main(String[] args) {


    }

}
