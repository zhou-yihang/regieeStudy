package com.powerbyte.regiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powerbyte.regiee.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
