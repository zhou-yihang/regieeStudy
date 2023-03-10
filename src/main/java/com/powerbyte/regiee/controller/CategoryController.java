package com.powerbyte.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerbyte.regiee.bean.Category;
import com.powerbyte.regiee.common.R;
import com.powerbyte.regiee.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/8 10:40
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 添加分类
     * @param category 分类
     * @return 添加结果
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类[{}]", category.getName());
        categoryService.save(category);
        return R.success("新增菜品成功");
    }

    /**
     * 查询所有分类
     * @param page 分页参数
     * @param pageSize 分页参数
     * @return 分类列表
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize) {
        log.info("分页查询菜品,当前页:{},每页条数:{}", page, pageSize);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //添加排序条件：按sort字段升序排列
        wrapper.orderByAsc(Category::getSort);
        //查询菜品列表
        categoryService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据ids删除分类
     * @param ids 分类ids
     * @return 分类列表
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类,ids:{}", ids);
        //删除分类
        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类删除成功");
    }

    /**
     * 根据id修改分类
     * @param category 分类
     * @return 分类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("更新分类,分类:{}", category);
        categoryService.updateById(category);
        return R.success("分类更新成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category 分类
     * @return 分类
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //构造查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //添加类型条件
        wrapper.eq(category.getId() != null, Category::getType, category.getType());
        //添加排序条件
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询分类列表
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
