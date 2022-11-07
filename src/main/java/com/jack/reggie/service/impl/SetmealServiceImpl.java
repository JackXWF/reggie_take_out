package com.jack.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggie.common.CustomException;
import com.jack.reggie.entity.Setmeal;
import com.jack.reggie.entity.SetmealDish;
import com.jack.reggie.entity.SetmealDto;
import com.jack.reggie.mapper.SetmealMapper;
import com.jack.reggie.service.SetmealDishService;
import com.jack.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新建套餐
     *
     * @param setmealDto
     */
    @Override
    public void saveWithSetmeal(SetmealDto setmealDto) {

        this.save(setmealDto);//保存套餐信息

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Override
    public void deleteWithSetmeal(List ids) {

        //删除套餐
        //select count(*) from setmeal where id in (1,2,3) and status=1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(Setmeal::getId, ids);//id in (1,2,3)
        queryWrapper.eq(Setmeal::getStatus, 1);//status=1

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new CustomException("该套餐在销售中，不能删除");
        }

        this.removeByIds(ids);

        //删除关联表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();

        queryWrapper2.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(queryWrapper2);

    }
}
