package com.ctspcl.secure.stater.config;

import com.ctspcl.common.exception.BizException;
import com.ctspcl.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/3
 **/
public class SecretCoreValidator {

    private SecretProperty secretConfig;

    private final ObjectMapper jacksonMapper = new ObjectMapper();

    public SecretCoreValidator(SecretProperty secretConfig) {
        this.secretConfig = secretConfig;
    }

    public boolean preValid(String ...urls){
        if (urls == null ||urls.length <= 0){
            throw new BizException("500001","请求URL不能为空");
        }
        for (String url : urls) {
            for (String uri : secretConfig.getBlacklist()) {
                if (url.equals(uri)){
                    return true;
                }
            }
        }
        return false;
    }

    public void validSignature(String realBody,String sign) throws Exception {
        //String sortStr = getSortData(realBody);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(realBody.getBytes());
        String encode = new BigInteger(1, md.digest()).toString(16);
        if (!sign.equals(encode)){
            throw new BizException(ErrorCode.SERVER_ERROR.getCode(),"签名异常");
        }
    }

    private String getSortData(String realBody) throws IOException {
        Map<String,Object> map = jacksonMapper.readValue(realBody,Map.class);
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        List<String> sortKey = Lists.newArrayList();
        entrySet.stream().forEach(entry -> sortKey.add(entry.getKey()));
        Collections.sort(sortKey);
        StringBuilder stb = new StringBuilder();
        sortKey.stream().forEach(key -> stb.append(key).append("=").append(map.get(key)).append("&"));
        return stb.substring(0,stb.lastIndexOf("&"));
    }


}
