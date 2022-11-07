package com.jack.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggie.entity.Dish;
import com.jack.reggie.entity.DishDto;
import com.jack.reggie.entity.DishFlavor;
import com.jack.reggie.mapper.DishMapper;
import com.jack.reggie.service.DishFlavorService;
import com.jack.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品的基本信息到dish
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();


        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dishId);
        }

        /*flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
*/
        //保存菜品的基本口味到dish_flavor
        dishFlavorService.saveBatch(flavors);

    }


    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);//查询菜品基本信息

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());//where dishId=dishDto.getId()

        dishFlavorService.remove(queryWrapper);

        //添加现在的口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item -> {
            item.setDishId(dishDto.getId());
            return item;
        })).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);

    }
}
