# API请求响应加解密
##### 单体应用层
* 基于spring的控制器增强技术（ControllerAdvice、RequestBodyAdvice和ResponseBodyAdvice）
* 直接将starter引入单体应用，配置SecretProperty类即可；
* 使用实例暂无
##### 网关层
* 基于servlet在网关层实现，此处网关采用zuul。
* 直接将starter引入网关，配置SecretProperty类即可；
* 仅支持json协议
* 使用实例参考test目录的html文件
```text
1、请求时，需要对请求体进行构建。将原有的请求体封装为json对象
2、再将json对象加密
3、将加密后的json串，以新的json格式数据传递服务器
4、网关服务器解密且转发给指定应用后，返回结果
5、网关过滤器，得到结果，对结果进行加密
6、最终网关将加密后的结果返回前端，前端解密得到响应数据
```