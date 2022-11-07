package com.jack.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggie.entity.Dish;
import com.jack.reggie.entity.DishDto;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味
    public void saveWithFlavor(DishDto dishDto);

    //查询菜品数据
    public DishDto getByIdWithFlavor(Long id);

    //修改菜品
    public void updateWithFlavor(DishDto dishDto);
}
