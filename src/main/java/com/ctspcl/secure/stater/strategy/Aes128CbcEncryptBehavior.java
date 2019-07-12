package com.ctspcl.secure.stater.strategy;

import com.ctspcl.common.exception.BizException;
import com.ctspcl.common.general.Result;
import com.ctspcl.secure.stater.DecryptUtil;
import com.ctspcl.secure.stater.MarkConstant;
import com.ctspcl.secure.stater.RequestUtil;
import com.ctspcl.secure.stater.config.SecretProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/10
 **/
@Slf4j
public class Aes128CbcEncryptBehavior implements FilterBehavior,ShouldFilterBehavior {

    private SecretProperty secretProperty;

    private final ObjectMapper jacksonMapper = new ObjectMapper();

    public Aes128CbcEncryptBehavior(SecretProperty secretProperty) {
        this.secretProperty = secretProperty;
    }

    @Override
    public Object doRun() {
        RequestContext context = RequestContext.getCurrentContext();
        String responseBody = RequestUtil.getBodyString(context);
        if (StringUtils.hasText(responseBody)){
            try {
                Result result = jacksonMapper.readValue(responseBody,Result.class);
                if (result.isSuccess()){
                    Object data = result.getData();
                    if (data != null && !(data instanceof Void)){
                        String encodeStr = DecryptUtil.encrypt(jacksonMapper.writeValueAsString(data),secretProperty.getPriKeySecret(),
                                secretProperty.getIvKeySecret(),ContentType.APPLICATION_JSON.getCharset().name());
                        result.setData(new BASE64Encoder()
                                .encode(encodeStr.getBytes())
                                .replaceAll(System.getProperty(DecryptUtil.LINE_SEPARATOR), ""));
                    }
                }
                context.setResponseBody(jacksonMapper.writeValueAsString(result));
            }catch (Exception e){
                log.error("Z00007","响应处理开小差");
                throw new BizException("Z00008","响应处理开小差");
            }
        }
        HttpServletResponse response = context.getResponse();
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(ContentType.APPLICATION_JSON.getCharset().name());
        return null;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getBoolean(MarkConstant.SecretAlgorithmMark.AES.toString())
                && context.getBoolean(MarkConstant.FilterMark.AES_REQUEST_FILTER.toString());
    }
}
