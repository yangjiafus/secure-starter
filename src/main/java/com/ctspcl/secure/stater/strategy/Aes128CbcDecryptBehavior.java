package com.ctspcl.secure.stater.strategy;

import com.ctspcl.common.exception.BizException;
import com.ctspcl.secure.stater.DecryptUtil;
import com.ctspcl.secure.stater.MarkConstant;
import com.ctspcl.secure.stater.RequestUtil;
import com.ctspcl.secure.stater.SecretCoreValidator;
import com.ctspcl.secure.stater.config.SecretProperty;
import com.ctspcl.secure.stater.wrapper.SecretRequestWrapper;
import com.netflix.zuul.context.RequestContext;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/10
 **/
public class Aes128CbcDecryptBehavior implements FilterBehavior {

    private SecretProperty secretProperty;

    private SecretCoreValidator validator;

    public Aes128CbcDecryptBehavior(SecretProperty secretProperty, SecretCoreValidator validator) {
        this.secretProperty = secretProperty;
        this.validator = validator;
    }

    @Override
    public Object doRun() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String body = RequestUtil.getBodyString(request);
        String realBody = null;
        if (StringUtils.hasText(body)){
            try {
                body = URLDecoder.decode(body,"utf-8");
                int index = body.indexOf("=");
                String encryptBody = body.substring(index + 1,body.indexOf("&") != -1?body.indexOf("&"):body.length());
                realBody = DecryptUtil.decrypt(new String(new BASE64Decoder().decodeBuffer(encryptBody)),
                        secretProperty.getPriKeySecret().getBytes(),secretProperty.getIvKeySecret().getBytes(),ContentType.APPLICATION_JSON.getCharset().name());
                String sign = RequestUtil.getSign(request);
                validator.validSignature(realBody,sign);
            }catch (Exception e){
                if (e instanceof BizException){
                    BizException bizException = (BizException)e;
                    throw new BizException(bizException.getCode(),bizException.getMessage());
                }
                throw new BizException("Z00005","请求解密BODY开小差,拒绝服务");
            }
        }
        if (StringUtils.hasText(realBody)){
            context.setRequest(new SecretRequestWrapper(request,realBody));
        }else {
            context.setRequest(new SecretRequestWrapper(request,null));
        }
        context.set(MarkConstant.SecretAlgorithmMark.AES.toString(),true);
        context.set(MarkConstant.FilterMark.AES_REQUEST_FILTER.toString(),true);
        return null;
    }
}
