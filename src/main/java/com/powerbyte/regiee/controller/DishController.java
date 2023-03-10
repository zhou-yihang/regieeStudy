package com.powerbyte.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerbyte.regiee.bean.Category;
import com.powerbyte.regiee.bean.Dish;
import com.powerbyte.regiee.common.R;
import com.powerbyte.regiee.dto.DishDto;
import com.powerbyte.regiee.service.CategoryService;
import com.powerbyte.regiee.service.DishFlavorService;
import com.powerbyte.regiee.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/21 16:36
 */
@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    /**
     * 添加菜品
     * @param dishDto 菜品
     * @return 添加结果
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品[{}]", dishDto.getName());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 查询所有菜品
     * @param page 分页参数
     * @param pageSize 分页参数
     * @return 菜品列表
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize, String name) {
        log.info("分页查询菜品,当前页:{},每页条数:{}", page, pageSize);
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> pageInfoDto = new Page<>();
        //构造查询条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null,Dish::getName, name);
        //添加排序条件：按sort字段升序排列
        wrapper.orderByAsc(Dish::getUpdateTime);
        //查询菜品列表
        dishService.page(pageInfo, wrapper);
        //拷贝数据
        BeanUtils.copyProperties(pageInfo, pageInfoDto, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> recordsDto = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        pageInfoDto.setRecords(recordsDto);
        return R.success(pageInfoDto);
    }

    /**
     * 查询菜品
     * @param id 菜品id
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public R<DishDto> get (@PathVariable Long id) {
        DishDto dishDto = dishService.getDishAndFlavorById(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto 菜品
     * @return 添加结果
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品[{}]", dishDto.getName());
        dishService.updateDishAndFlavorById(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 根据条件查询菜品
     * @param dish 菜品
     * @return 查询结果
     */
    @GetMapping("/list")
    public R<List<Dish>> list (Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //根据id查询
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //只查询状态为1的：启售菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //查询
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }
}
