package com.ctspcl.secure.stater;

import com.ctspcl.common.exception.BizException;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/10
 **/
@Slf4j
public class RequestUtil {

    public static final String AUTHORIZATION = "Authorization";

    public static final String SIGN = "sign";


    public static String getSign(HttpServletRequest request){
        return request.getHeader(SIGN);
    }

    public static String getAuthorization(HttpServletRequest request){
        return request.getHeader(AUTHORIZATION);
    }

    public static String getBodyString(HttpServletRequest request) {
        String requestBody = null;
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
//            if (inputStream.available() > 0){
                requestBody = StreamUtils.copyToString(inputStream, Charset.forName(DecryptUtil.CHARSET_UTF_8));
//            }
        } catch (IOException e) {
            log.error("读取请求体异常，服务暂无响应");
            throw new BizException("Z00001","读取请求开小差，服务暂无响应");
        }
        return requestBody;
    }

    public static String getBodyString(RequestContext context) {
        String responseBody = null;
        InputStream inputStream = context.getResponseDataStream();
        try {
//            if (inputStream.available() > 0){
                responseBody = StreamUtils.copyToString(inputStream, Charset.forName(DecryptUtil.CHARSET_UTF_8));
//            }
        }catch (Exception e){
            log.error("读取响应体异常，服务暂无响应");
            throw new BizException("Z00006","读取响应体异常，服务暂无响应");
        }
        return responseBody;
    }


}
