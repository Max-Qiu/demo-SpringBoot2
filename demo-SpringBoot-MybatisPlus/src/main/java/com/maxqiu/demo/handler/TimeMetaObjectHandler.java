package com.maxqiu.demo.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * @author Max_Qiu
 */
@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {

    /**
     * 根据 MySQL datetime 字段设置的长度值设置精度
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSS");

    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("插入时自动填充");

        // 方案1 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        // 解决 Java 和 MySQL 时间精度不一样的问题（即使用MySQL的时间精度格式化一下）
        // LocalDateTime parse = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);
        // this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, parse);

        // 方案2 起始版本 3.3.3(推荐)
        // this.strictInsertFill(metaObject, "createTime", new Supplier<LocalDateTime>() {
        // @Override
        // public LocalDateTime get() {
        // return LocalDateTime.now();
        // }
        // }, LocalDateTime.class);

        // 方案3 3.3.0 该方法有bug
        // this.fillStrategy(metaObject, "createTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("修改时自动填充");

        // 方案1 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 方案2 起始版本 3.3.3(推荐)
        // this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        // 方案3 3.3.0 该方法有bug
        // this.fillStrategy(metaObject, "updateTime", LocalDateTime.now());
    }
}
