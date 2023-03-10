package com.powerbyte.regiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powerbyte.regiee.bean.Setmeal;
import com.powerbyte.regiee.bean.SetmealDish;
import com.powerbyte.regiee.dto.SetmealDto;
import com.powerbyte.regiee.mapper.SetmealMapper;
import com.powerbyte.regiee.service.SetmealDishService;
import com.powerbyte.regiee.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 11:43
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时关联套餐与菜品的关联关系
     * @param setmealDto 数据传输对象
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        //保存关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
