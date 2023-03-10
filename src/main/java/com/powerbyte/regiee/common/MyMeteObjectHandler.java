package com.powerbyte.regiee.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.time.LocalDateTime;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/7 17:00
 */
@Slf4j
@Component
public class MyMeteObjectHandler implements MetaObjectHandler {

    /**
     * 插入数据时自动填充创建人和创建时间
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertFill");

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("createUser", BeanContext.getCurrentId());
        metaObject.setValue("updateUser", BeanContext.getCurrentId());
    }

    /**
     * 更新数据时自动填充更新人和更新时间
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updateFill");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BeanContext.getCurrentId());
    }
}