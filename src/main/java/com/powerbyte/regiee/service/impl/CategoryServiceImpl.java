package com.powerbyte.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powerbyte.regiee.bean.Category;
import com.powerbyte.regiee.bean.Dish;
import com.powerbyte.regiee.bean.Setmeal;
import com.powerbyte.regiee.common.CustomException;
import com.powerbyte.regiee.mapper.CategoryMapper;
import com.powerbyte.regiee.service.CategoryService;
import com.powerbyte.regiee.service.DishService;
import com.powerbyte.regiee.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 10:38
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要判断该分类下是否还有菜品，如果有，则不能删除
     * @param id id
     */
    @Override
    public void remove(Long id) {

        // 判断该分类下是否还有菜品，如果有，则不能删除
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if(dishCount > 0){
            throw new CustomException("该分类下还有菜品，不能删除");
        }
        // 判断该分类下是否还有套餐，如果有，则不能删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if(setmealCount > 0){
            throw new CustomException("该分类下还有套餐，不能删除");
        }
        // 正常删除分类
        super.removeById(id);
    }
}
