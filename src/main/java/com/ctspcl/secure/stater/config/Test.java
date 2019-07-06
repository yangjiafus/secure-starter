package com.ctspcl.secure.stater.config;


import com.fasterxml.jackson.databind.ObjectMapper;
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
        String json = "{\"idCard\":\"500241198707110338\",\"name\":\"杨佳富\",\"productType\":1,\"useType\":\"0\"}";
        String encode = DecryptUtil.encrypt(json,"fdshdfsklsjdesdf","poskhegsnlekhsdf",null);
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
        String decode = DecryptUtil.decrypt(base64Decode,"fdshdfsklsjdesdf".getBytes(),"poskhegsnlekhsdf".getBytes(),null);
        System.out.println("解密: " + decode);


        ObjectMapper jacksonMapper = new ObjectMapper();
        Map map = jacksonMapper.readValue(json,Map.class);
        System.out.println("MAp：  "+map);
    }
}
