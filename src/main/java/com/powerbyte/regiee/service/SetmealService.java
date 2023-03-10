package com.powerbyte.regiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powerbyte.regiee.bean.Setmeal;
import com.powerbyte.regiee.dto.SetmealDto;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 11:38
 */

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时关联套餐与菜品的关联关系
     * @param setmealDto 数据传输对象
     */
    void saveWithDish(SetmealDto setmealDto);
}
