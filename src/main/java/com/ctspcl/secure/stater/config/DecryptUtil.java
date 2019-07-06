package com.ctspcl.secure.stater.config;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author JiaFu.yang
 * @description  AES-128-CBC，数据采用PKCS#7填充
 * @date 2019/7/4
 **/
public class DecryptUtil {

    public static String decrypt(byte[] encryptedDataByte, String charsetName, byte[] sessionKeyByte, byte[] ivByte) throws Exception {
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(sessionKeyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            byte[] original = cipher.doFinal(encryptedDataByte);
            String originalString;
            if (StringUtils.hasText(charsetName)){
                originalString = new String(original,charsetName);
            }else {
                originalString = new String(original);
            }
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String decrypt(String encryptedData,byte[] sessionKey,byte[] iv,String charsetName) throws Exception{
        return DecryptUtil.decrypt(new BASE64Decoder().decodeBuffer(encryptedData),charsetName,sessionKey,iv);
    }

    public static String encrypt(String encryptedData,String sessionKey,String iv,String charsetName) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sessionKey.getBytes();
        SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivp);
        byte[] encrypted;
        if (StringUtils.hasText(charsetName)){
            encrypted = cipher.doFinal(encryptedData.getBytes(charsetName));
        }else {
            encrypted = cipher.doFinal(encryptedData.getBytes());
        }
        return new BASE64Encoder().encode(encrypted).replaceAll(System.getProperty("line.separator"), "");
    }

}
