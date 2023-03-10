package com.powerbyte.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerbyte.regiee.bean.Employee;
import com.powerbyte.regiee.common.R;
import com.powerbyte.regiee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/6 11:03
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 登录
     * @param request 请求
     * @param employee 用户
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 密码加密
        String password = employee.getPassword();
        //MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询用户信息根据用户名
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);

        // 判断用户是否存在
        if (emp == null) {
            return R.error("用户名不存在!");
        }
        // 判断密码是否正确
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误!");
        }
        // 判断用户是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("用户已被禁用!");
        }

        //将用户信息放入session
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    /**
     * 退出
     * @param request 请求对象
     * @return 退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("已安全退出!");
    }

    /**
     * 添加员工
     * @param employee 员工信息
     * @return 注册结果
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("添加员工,员工信息:{}", employee.toString());
        //设置初始密码为身份证后6位，并MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getIdNumber().substring(employee.getIdNumber().length() - 6).getBytes()));

        //设置创建时间和修改时间为当前时间
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //设置创建人和修改人为当前登录用户
        //employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        //employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));

        //添加员工
        employeeService.save(employee);
        String msg = "添加员工[" + employee.getUsername() + "]成功!";
        return R.success(msg);
    }

    /**
     * 查询员工列表
     * @param page 分页信息
     * @param pageSize 每页显示条数
     * @param name 员工姓名
     * @return 员工列表
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize, String name) {
        log.info("分页查询员工,当前页:{},每页条数:{},查询条件(姓名):{}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造查询条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //添加查询条件：姓名
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件：按修改时间降序排序
        wrapper.orderByDesc(Employee::getUpdateTime);
        //查询员工列表
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 查询员工详情
     * @param request 请求对象
     * @param employee 员工
     * @return 员工详情
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工,员工信息:{}", employee.toString());
        //设置修改时间为当前时间
        employee.setUpdateTime(LocalDateTime.now());
        //设置修改人为当前登录用户
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        //修改员工
        employeeService.updateById(employee);
        String msg = "修改员工[" + employee.getUsername() + "]成功!";
        return R.success(msg);
    }

    /**
     * 查询员工详情
     * @param id 员工id
     * @return 员工详情
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("查询员工详情,员工id:{}", id);
        //查询员工详情
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("员工不存在!");
        }
        return R.success(employee);
    }
}
