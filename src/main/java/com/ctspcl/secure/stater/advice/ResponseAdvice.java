package com.ctspcl.secure.stater.advice;

import com.ctspcl.common.general.Result;
import com.ctspcl.secure.stater.DecryptUtil;
import com.ctspcl.secure.stater.ResolveUriHelper;
import com.ctspcl.secure.stater.SecretCoreValidator;
import com.ctspcl.secure.stater.config.SecretProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sun.misc.BASE64Encoder;

/**
 * @author JiaFu.yang
 * @description 响应统一处理
 * @date 2019/7/3
 **/
@Slf4j
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    private final ObjectMapper jacksonMapper = new ObjectMapper();
    @Autowired
    private SecretProperty secretConfig;

    @Autowired
    private SecretCoreValidator validator;

    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        return validator.preValid(ResolveUriHelper.getFullUri(methodParameter));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        log.info("响应请求，返回数据加密：{}",body);
        if (null != body) {
            MappingJacksonValue jacksonValue = (MappingJacksonValue)body;
            if (jacksonValue.getValue() instanceof Result){
                Result result = (Result)jacksonValue.getValue();
                if (result.isSuccess()){
                    Object data = result.getData();
                    if (data == null || data instanceof Void){
                        return body;
                    }
                    try {
                        //对data加密
                        String encodeStr = DecryptUtil.encrypt(jacksonMapper.writeValueAsString(data),secretConfig.getPriKeySecret(),secretConfig.getIvKeySecret(),null);
                        result.setData(new BASE64Encoder().encode(encodeStr.getBytes()).replaceAll(System.getProperty("line.separator"), ""));
                        jacksonValue.setValue(result);
                        return jacksonValue;
                    }catch (Exception e) {
                        log.warn("加密数据失败.", e);
                    }
                }
            }
        }
        return body;
    }

}
