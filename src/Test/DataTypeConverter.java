package Test;

import java.nio.ByteBuffer;

/**
 *
 * @author Xunhua Wang (ecsha2014@gmail.com). All rights reserved
 * @date 03/24/2020
 *
 */
public class DataTypeConverter {
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String hexBytesToString (byte[] inBytes) {
        StringBuffer sb = new StringBuffer();
        for (byte ki : inBytes) sb.append (String.format("%02X", ki & 0xff));
        return sb.toString();
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }
    //
    // NOT USED anymore
    // https://stackoverflow.com/questions/1026761/how-to-convert-a-byte-array-to-its-numeric-value-java/1026804
    // Least significant byte first
    //

    public static int bytesToInt (byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }
}