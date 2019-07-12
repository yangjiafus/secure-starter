package com.ctspcl.secure.stater.strategy;

import com.ctspcl.secure.stater.RequestUtil;
import com.ctspcl.secure.stater.config.SecretProperty;
import com.netflix.zuul.context.RequestContext;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
@AllArgsConstructor
public abstract class ShouldFilter {

    private SecretProperty secretProperty;

    private final ShouldFilterBehavior defaultBehavior = ()->{
        //开关是否开启
        if (!isOnOff()){
            return false;
        }
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //是否在黑名单
        if (!isInBlacklist(request)){
            if (!secretProperty.getOnOffAuthorize()){
                return false;
            }
            return hasAuthorize(request);
        }
        return true;
    };
    /**
     * 能否执行过滤器:
     * 启动过滤开关且有AUTHORIZE或在黑名单内的URI需要 执行过滤
     * @return
     * **/
    public abstract boolean canFilter();

    protected boolean defaultShouldFilter(){
        return defaultBehavior.shouldFilter();
    }

    protected boolean isOnOff(){
        return secretProperty.getOnOff();
    }

    protected boolean hasAuthorize(HttpServletRequest request){
        return StringUtils.hasText(RequestUtil.getAuthorization(request));
    }

    protected boolean isInBlacklist(HttpServletRequest request){
        List<String> uriList = secretProperty.getBlacklist();
        String uri = request.getRequestURI();
        for (String ur : uriList) {
            if (ur.contains(uri)){
                return true;
            }
        }
        return false;
    }

}
