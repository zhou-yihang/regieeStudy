package com.powerbyte.regiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powerbyte.regiee.bean.Category;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 10:37
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
