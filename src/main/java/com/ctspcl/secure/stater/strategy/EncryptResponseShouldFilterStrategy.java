package com.ctspcl.secure.stater.strategy;

import com.ctspcl.secure.stater.config.SecretProperty;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
public class EncryptResponseShouldFilterStrategy extends ShouldFilter {

    private SecretProperty secretProperty;

    private ShouldFilterBehavior shouldFilterBehavior;

    public EncryptResponseShouldFilterStrategy(SecretProperty secretProperty, ShouldFilterBehavior shouldFilterBehavior) {
        super(secretProperty);
        this.secretProperty = secretProperty;
        this.shouldFilterBehavior = shouldFilterBehavior;
    }

    @Override
    public boolean canFilter() {
        if (shouldFilterBehavior == null){
            return defaultShouldFilter();
        }
        return shouldFilterBehavior.shouldFilter();
    }
}
