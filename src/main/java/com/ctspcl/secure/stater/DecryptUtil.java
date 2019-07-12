package com.ctspcl.secure.stater;

import com.ctspcl.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DecryptUtil {

    public static final String CHARSET_UTF_8 = "UTF-8";

    public static final String KEY_SPEC_AES = "AES";

    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static final String LINE_SEPARATOR = "line.separator";





    public static String decrypt(byte[] encryptedDataByte, String charsetName, byte[] sessionKeyByte, byte[] ivByte) throws Exception {
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(sessionKeyByte, KEY_SPEC_AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
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
            log.error("解密请求Body失败：{}",new String(encryptedDataByte));
            throw new BizException("Z00002","请求解密开小差，拒绝服务");
        }
    }

    public static String decrypt(String encryptedData,byte[] sessionKey,byte[] iv,String charsetName) throws Exception{
        return DecryptUtil.decrypt(new BASE64Decoder().decodeBuffer(encryptedData),charsetName,sessionKey,iv);
    }

    public static String encrypt(String encryptedData,String sessionKey,String iv,String charsetName) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] raw = sessionKey.getBytes();
        SecretKeySpec sKeySpec = new SecretKeySpec(raw, KEY_SPEC_AES);
        IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivp);
        byte[] encrypted;
        if (StringUtils.hasText(charsetName)){
            encrypted = cipher.doFinal(encryptedData.getBytes(charsetName));
        }else {
            encrypted = cipher.doFinal(encryptedData.getBytes());
        }
        return new BASE64Encoder().encode(encrypted).replaceAll(System.getProperty(LINE_SEPARATOR), "");
    }

}
