package com.ctspcl.secure.stater.advice;

import com.ctspcl.common.exception.BizException;
import com.ctspcl.common.exception.ErrorCode;
import com.ctspcl.common.log.annotation.EnableLog;
import com.ctspcl.secure.stater.DecryptUtil;
import com.ctspcl.secure.stater.ResolveUriHelper;
import com.ctspcl.secure.stater.SecretCoreValidator;
import com.ctspcl.secure.stater.config.SecretProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import static com.ctspcl.secure.stater.ResolveUriHelper.SIGN;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/3
 **/
@Slf4j
@RestControllerAdvice
public class RequestAdvice implements RequestBodyAdvice {

    @Autowired
    private SecretCoreValidator validator;
    @Autowired
    private SecretProperty secretConfig;

    @EnableLog
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //获取方法的请求URL
        String fullUri = ResolveUriHelper.getFullUri(methodParameter);
        if (!methodParameter.hasParameterAnnotation(RequestBody.class)){
            log.warn("请求 {} 不存在@RequestBody注解",fullUri);
            return false;
        }
        return validator.preValid(fullUri);
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        return new HttpInputMessage() {
            @EnableLog
            @Override
            public InputStream getBody() throws IOException {
                final String sign = inputMessage.getHeaders().get(SIGN)!=null?inputMessage.getHeaders().get(SIGN).get(0):null;
                InputStream inputStream  = inputMessage.getBody();
                if (inputStream.available() > 0){
                    if (!StringUtils.hasText(sign)){
                        throw new BizException(ErrorCode.SERVER_ERROR.getCode(),"接收请求，无效的签名");
                    }
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    String bodyStr = new String(bytes);
                    log.info("接收请求，解密数据：{}",bodyStr);
                    try {
                        //对整个body进行解密
                        String realBody = DecryptUtil.decrypt(new String(new BASE64Decoder().decodeBuffer(bodyStr)),
                                secretConfig.getPriKeySecret().getBytes(),secretConfig.getIvKeySecret().getBytes(),null);
                        validator.validSignature(realBody,sign);
                        return new ByteArrayInputStream(realBody.getBytes("utf-8"));
                    } catch (Exception e) {
                        throw new BizException(ErrorCode.SERVER_ERROR.getCode(),"接收请求，解密数据异常");
                    }
                }
                return inputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }



}
