package com.maxqiu.demo.config;

import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * 自定义的缓存键生成器
 *
 * @author Max_Qiu
 */
@Component
public class CacheKeyGenerator implements KeyGenerator {
    // key分隔符
    private static final String KEY_SEPARATOR = "::";
    // params分隔符
    private static final String PARAMS_SEPARATOR = ",";

    /**
     * 生成器
     *
     * @param target
     *            目标实例
     * @param method
     *            被调用的方法
     * @param params
     *            方法参数(扩展了任何var-args)
     */
    @Override
    public String generate(Object target, Method method, Object... params) {
        // 键
        StringBuilder builder = new StringBuilder(30);
        // 类名（可选）
        // builder.append(target.getClass().getSimpleName());
        // builder.append(separator);
        // 方法名
        builder.append(method.getName());
        if (params.length == 0) {
            return builder.toString();
        }
        // 参数
        builder.append(KEY_SEPARATOR);
        builder.append(generateKey(params));
        return builder.toString();
    }

    private String generateKey(Object... params) {
        StringJoiner sj = new StringJoiner(PARAMS_SEPARATOR);
        for (Object elem : params) {
            sj.add(String.valueOf(elem));
        }
        return sj.toString();
    }
}
