package com.powerbyte.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powerbyte.regiee.bean.Dish;
import com.powerbyte.regiee.bean.DishFlavor;
import com.powerbyte.regiee.dto.DishDto;
import com.powerbyte.regiee.mapper.DishMapper;
import com.powerbyte.regiee.service.DishFlavorService;
import com.powerbyte.regiee.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 11:39
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>  implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品，同时添加菜品的口味：需要操作两张表 dish 和 dish_flavor
     * @param dishDto 菜品:数据传输对象
     */
    @Override
    @Transient
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息
        this.save(dishDto);

        //保存菜品的口味信息
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map(flavor -> {
            flavor.setDishId(dishId);
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询 菜品信息 和 菜品的口味 信息
     * @param id 菜品id
     * @return 菜品:数据传输对象
     */
    @Override
    public DishDto getDishAndFlavorById(Long id) {
        DishDto dishDto = new DishDto();
        //根据id查询菜品信息
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish, dishDto);
        //根据id查询菜品的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 根据id修改 菜品信息 和 菜品的口味 信息
     * @param dishDto 菜品:数据传输对象
     */
    @Override
    @Transient
    public void updateDishAndFlavorById(DishDto dishDto) {
        //修改菜品的基本信息
        this.updateById(dishDto);
        //删除菜品的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(wrapper);
        //添加菜品的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map(flavor -> {
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
