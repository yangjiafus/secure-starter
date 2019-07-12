package com.ctspcl.secure.stater.filter;

import com.ctspcl.secure.stater.strategy.FilterBehavior;
import com.ctspcl.secure.stater.strategy.ShouldFilter;
import com.netflix.zuul.ZuulFilter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
public class EncryptResponseFilter extends ZuulFilter {

    private ShouldFilter shouldFilter;

    private FilterBehavior filterBehavior;

    public EncryptResponseFilter(ShouldFilter shouldFilter, FilterBehavior filterBehavior) {
        this.shouldFilter = shouldFilter;
        this.filterBehavior = filterBehavior;
    }

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 100;
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
