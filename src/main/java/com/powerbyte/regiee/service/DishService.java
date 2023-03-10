package com.powerbyte.regiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powerbyte.regiee.bean.Dish;
import com.powerbyte.regiee.dto.DishDto;


/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 11:39
 */
public interface DishService extends IService<Dish> {

    // 添加菜品，同时添加菜品的口味：需要操作两张表 dish 和 dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据id查询 菜品信息 和 菜品的口味 信息
    DishDto getDishAndFlavorById(Long id);

    //根据id修改 菜品信息 和 菜品的口味 信息
    void updateDishAndFlavorById(DishDto dishDto);
}
