package com.maxqiu.demo.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义解析器，用于处理类型是 CurrUserVO 的参数
 *
 * @author Max_Qiu
 */
public class CurrUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只有标注有 CurrUser 注解，并且数据类型是 CurrUserVO 的才给与处理
        CurrUser ann = parameter.getParameterAnnotation(CurrUser.class);
        Class<?> parameterType = parameter.getParameterType();
        return ann != null && CurrUserVO.class.isAssignableFrom(parameterType);
    }

    @Override
    public CurrUserVO resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 从请求头中拿到token
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) {
            // 此处不做异常处理，校验token因放在拦截器中处理
            return null;
        }
        // 此处作为测试写死一个用户，实际使用时调用service获取当前用户
        CurrUserVO userVO = new CurrUserVO();
        userVO.setId(1L);
        userVO.setName("tom");
        return userVO;
    }
}
