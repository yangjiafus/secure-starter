package com.ctspcl.secure.stater;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/5
 **/
public class Test {

    public static void main(String[] args) throws Exception{
        String json = "{\"productCode\":\"210101\"}";
        String encode = DecryptUtil.encrypt(json,"fdshdfsklsjdesdf","poskhegsnlekhsdf",ContentType.APPLICATION_JSON.getCharset().name());
        System.out.println("ASE 加密: "  + encode);

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(json.getBytes());
        String md5Encode = new BigInteger(1, md.digest()).toString(16);
        System.out.println("md5 加密: "  + md5Encode);
        String base64Encode = new BASE64Encoder().encode(encode.getBytes());
        base64Encode = base64Encode.replaceAll(System.getProperty("line.separator"), "");
        System.out.println("base64 加密: "  + base64Encode);
        String base64Decode = new String (new BASE64Decoder().decodeBuffer(base64Encode));
        System.out.println("base64 解密: "  + base64Decode);

        base64Decode = "udxEzaJR/BUlWCANtTWslj+gp5KEb91F7Aet0AXY3ixqofb8AH4TPmZqN6guGhq0xzjpYNiUgWuIRudhBp+50vx96cmqNtwSZfJL5XBH5EEIdD6Y3KiS9DOD7Pu59qAA2dqdHBuV3FYiXQB2m/t3HD2CBxgK47/HkdQGhYHOliYS+xiKLvyi3RPTLlt9dQET";
        String decode = DecryptUtil.decrypt(base64Decode,"fdshdfsklsjdesdf".getBytes(),"poskhegsnlekhsdf".getBytes(),ContentType.APPLICATION_JSON.getCharset().name());
        System.out.println("解密: " + decode);

        System.out.println("--end--");

    }
}
