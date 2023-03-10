package com.powerbyte.regiee.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/6 20:55
 *
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {Controller.class, RestController.class})
public class GlobalExceptionHandler {

    /**
     * 数据库用户名重复异常
     * @param e 异常
     * @return 异常信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handleException(SQLIntegrityConstraintViolationException e) {
        log.error("系统异常：{}", e.getMessage());
        if (e.getMessage().contains("Duplicate entry")) {
            String[] str = e.getMessage().split(" ");
            String msg = "[" + str[2] + "]已存在，请更换!";
            return R.error(msg);
        }
        return R.error("系统异常，请联系管理员");
    }

    /**
     * 删除分类异常
     * @param e 异常
     * @return 异常信息
     */
    @ExceptionHandler(CustomException.class)
    public R<String> handleException(CustomException e) {
        log.error("系统异常：{}",e.getMessage());
        return R.error(e.getMessage());
    }
}
