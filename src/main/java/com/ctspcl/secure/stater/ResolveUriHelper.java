package com.ctspcl.secure.stater;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/5
 **/
public class ResolveUriHelper {

    public static final String IV = "iv";

    public static final String SESSION_KEY = "sessionKey";

    public static final String SIGN = "sign";

    public static final String ONE = "/";

    public static final String TWO = "//";

    public static final String THREE = "///";

    public static String getFullUri(MethodParameter methodParameter){
        StringBuilder validUri = new StringBuilder();
        RequestMapping requestMapping = methodParameter.getMethod().getDeclaringClass().getDeclaredAnnotation(RequestMapping.class);
        if (requestMapping != null){
            String[] preUriArr = requestMapping.value();
            if (preUriArr!= null && preUriArr.length > 0){
                if (preUriArr[0].indexOf(ONE) == 0){
                    validUri.append(preUriArr[0].replaceFirst(ONE,"")).append(ONE);
                }else {
                    validUri.append(preUriArr[0]).append(ONE);
                }
            }
        }
        String[] uris = getAfterUri(methodParameter);
        if (uris != null && uris.length > 0){
            validUri.append(uris[0]);
        }
        String validURI = validUri.toString().intern();
        if (validURI.indexOf(TWO) != -1){
            validURI = validURI.replaceAll(TWO,ONE).intern();
        }
        if (validURI.indexOf(THREE) != -1){
            validURI = validURI.replaceAll(THREE,ONE).intern();
        }
        validURI.intern();
        return validURI;
    }

    public static String[] getAfterUri(MethodParameter methodParameter) {
        RequestMapping requestMapping = methodParameter.getMethod().getDeclaredAnnotation(RequestMapping.class);
        String[] uris = null;
        if (requestMapping != null){
            uris = requestMapping.value();
        }
        PostMapping postMapping = methodParameter.getMethod().getDeclaredAnnotation(PostMapping.class);
        if (postMapping != null){
            uris = postMapping.value();
        }
        GetMapping getMapping = methodParameter.getMethod().getDeclaredAnnotation(GetMapping.class);
        if (getMapping != null){
            uris = getMapping.value();
        }
        return uris;
    }

}
