package com.ctspcl.secure.stater;

/**
 * @author JiaFu.yang
 * @description 请求处理标记枚举
 * @date 2019/7/10
 **/
public interface MarkConstant {

    /**加密算法*/
    enum SecretAlgorithmMark {
        /**高级加密标准算法*/
        AES,
        /**非对称加密算法*/
        RSA
    }

    enum FilterMark {
        /**高级加密标准-解密过滤器*/
        AES_REQUEST_FILTER,
        /**高级加密标准-加密过滤器*/
        AES_RESPONESE_FILTER,
        /**非对称加密算法-加密*/
        RSA_REQUEST_FILTER,
        /**非对称加密算法-解密*/
        RSA_RESPONESE_FILTER
    }


}
