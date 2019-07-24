package com.ctspcl.secure.stater.strategy;

import com.ctspcl.secure.stater.config.SecretProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/9
 **/
@Slf4j
public class DecryptRequestShouldFilterStrategy extends ShouldFilter {

    private SecretProperty secretProperty;

    private ShouldFilterBehavior shouldFilterBehavior;

    public DecryptRequestShouldFilterStrategy(SecretProperty secretProperty, ShouldFilterBehavior shouldFilterBehavior) {
        super(secretProperty);
        this.secretProperty = secretProperty;
        this.shouldFilterBehavior = shouldFilterBehavior;
    }

    @Override
    public boolean canFilter() {
        log.info("shouldFilterBehavior 是否为空：{}",shouldFilterBehavior == null);
        if (shouldFilterBehavior == null){
            return defaultShouldFilter();
        }
        return shouldFilterBehavior.shouldFilter();
    }
}
