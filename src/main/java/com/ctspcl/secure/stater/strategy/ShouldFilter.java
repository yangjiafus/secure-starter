package com.ctspcl.secure.stater.strategy;

import com.ctspcl.secure.stater.RequestUtil;
import com.ctspcl.secure.stater.config.SecretProperty;
import com.netflix.zuul.context.RequestContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
@Slf4j
@AllArgsConstructor
public abstract class ShouldFilter {

    private SecretProperty secretProperty;

    private final ShouldFilterBehavior defaultBehavior = ()->{
        log.info("开关是否开启:{}",isOnOff());
        //开关是否开启
        if (!isOnOff()){
            return false;
        }
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.info("是否存在AppId:{}",hasAppId(request));
        if (!hasAppId(request)){
            //是否在黑名单
            log.info("是否在黑名单内:{}",isInBlacklist(request));
            if (!isInBlacklist(request)){
                //是否开启 token验证
                log.info("开关是否token验证开启:{}",isOnOff());
                if (secretProperty.getOnOffAuthorize()){
                    return hasAuthorize(request);
                }else {
                    return false;
                }
            }
        }
        //有appId就一定要加密
        return true;
    };

    protected boolean hasAppId(HttpServletRequest request){
        return StringUtils.hasText(RequestUtil.getAppId(request));
    }

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
