package com.powerbyte.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerbyte.regiee.bean.Category;
import com.powerbyte.regiee.bean.Setmeal;
import com.powerbyte.regiee.common.R;
import com.powerbyte.regiee.dto.SetmealDto;
import com.powerbyte.regiee.service.CategoryService;
import com.powerbyte.regiee.service.SetmealDishService;
import com.powerbyte.regiee.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/24 10:26
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @Resource
    CategoryService categoryService;

    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 添加套餐
     * @param setmealDto 数据传输对象
     * @return 添加结果
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息:{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功");
    }

    /**
     * 套餐查询
     * @param page 页
     * @param pageSize 每页数据
     * @param name 模糊查询
     * @return 查询结果
     */
    @GetMapping("/page")
    public R<Page> page (int page,int pageSize,String name) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPageInfo = new Page<>();
        //分页构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加名字的模糊查询条件
        queryWrapper.like(name != null,Setmeal::getName,name);
        //根据更新时间降序排列
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPageInfo.setRecords(list);
        return R.success(dtoPageInfo);
    }
}
