package com.jack.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggie.common.CustomException;
import com.jack.reggie.entity.Category;
import com.jack.reggie.entity.Dish;
import com.jack.reggie.entity.Setmeal;
import com.jack.reggie.mapper.CategoryMapper;
import com.jack.reggie.service.CategoryService;
import com.jack.reggie.service.DishService;
import com.jack.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前的判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        //查询是否关联了菜品
        LambdaQueryWrapper<Dish> dishWrapper=new LambdaQueryWrapper();
        dishWrapper.eq(Dish::getCategoryId,id);//select * from dish where categoryId=id
        int count = dishService.count(dishWrapper);//select count(*) from dish where categoryId=id
        if(count>0){
            throw new CustomException("已关联菜品，无法删除");
        }
        //查询是否关联了套餐
        LambdaQueryWrapper<Setmeal> setMealWrapper=new LambdaQueryWrapper();
        setMealWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();

        if(count2>0){
            throw new CustomException("已关联套餐，无法删除");

        }


        //正常删除分类
        super.removeById(id);

    }
}
