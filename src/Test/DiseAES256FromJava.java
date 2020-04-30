package Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.Arrays;

import Rijndael.Rijndael_Algorithm;

public class DiseAES256FromJava {

    final static byte[] key256 = DataTypeConverter.hexStringToByteArray("4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32");
    final static byte[] key128 = DataTypeConverter.hexStringToByteArray("48404D635166546A576E5A7234753778");

    public void RijndaelCrypt(boolean encrypt, byte[] key, String inputFile, String outputFile) throws IOException, InvalidKeyException {
        byte[] cbcIV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 16 bytes

        int blockSize = 16;

        Object roundKeys = Rijndael_Algorithm.makeKey(key); // This creates the round key;

        byte[] inMsg = Files.readAllBytes(Paths.get(inputFile));
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int len = inMsg.length;
        int paddingOctet = blockSize - (len % blockSize);
        int totalLen = len + paddingOctet;

        byte[] paddedMsg = new byte[totalLen];
        System.arraycopy(inMsg, 0, paddedMsg, 0, len);

        // PKCS5 padding
        byte paddingOctetByte = (byte) (paddingOctet & 0xff);
        for (int i = 0; i < paddingOctet; i++) {
            paddedMsg[i + len] = paddingOctetByte;
        }

        int numOfBlocks = totalLen / blockSize;        // Each AES block has 16 bytes

        byte[] cipherText = new byte[totalLen];
        byte[] feedback = Arrays.copyOf(cbcIV, cbcIV.length);

        byte[] currentBlock = new byte[16];

        for (int i = 0; i < numOfBlocks; i++) {
            for (int j = 0; j < 16; j++) currentBlock[j] = (byte) (paddedMsg[i * 16 + j] ^ feedback[j]); // CBC feedback

            byte[] thisCipherBlock = Rijndael_Algorithm.blockEncrypt(currentBlock, 0, roundKeys);
            feedback = Arrays.copyOf(thisCipherBlock, thisCipherBlock.length);
            for (int j = 0; j < 16; j++) cipherText[i * 16 + j] = thisCipherBlock[j];
        }

        fos.write(cipherText);
        fis.close();
        bis.close();
        fos.close();
        bos.close();
    }

    public void testBBBDecrypt() {

    }

    public static void main(String[] args) {
        try {
            DiseAES256FromJava aes = new DiseAES256FromJava();
            aes.RijndaelCrypt(true, key256, "bbb.mp4", "bbb_256.bin");
            aes.RijndaelCrypt(true, key128, "bbb.mp4", "bbb_256.bin");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}