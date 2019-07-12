package com.ctspcl.secure.stater.filter;

import com.ctspcl.secure.stater.strategy.FilterBehavior;
import com.ctspcl.secure.stater.strategy.ShouldFilter;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
@Slf4j
public class DecryptRequestFilter extends ZuulFilter {


    private ShouldFilter shouldFilter;

    private FilterBehavior filterBehavior;

    public DecryptRequestFilter(ShouldFilter shouldFilter, FilterBehavior filterBehavior) {
        this.shouldFilter = shouldFilter;
        this.filterBehavior = filterBehavior;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -100;
    }

    @Override
    public boolean shouldFilter() {
        return shouldFilter.canFilter();
    }

    @Override
    public Object run() {
        return filterBehavior.doRun();
    }
}
