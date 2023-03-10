package com.powerbyte.regiee.dto;

import com.powerbyte.regiee.bean.Dish;
import com.powerbyte.regiee.bean.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
