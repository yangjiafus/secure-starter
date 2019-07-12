package com.ctspcl.secure.stater.strategy;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/10
 **/
public interface ShouldFilterBehavior {
    /**
     * 能否执行过滤器:
     * 启动过滤开关且有AUTHORIZE或在黑名单内的URI需要 执行过滤
     * @return
     * **/
    boolean shouldFilter();
}
