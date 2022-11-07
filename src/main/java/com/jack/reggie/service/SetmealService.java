package com.jack.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggie.entity.Setmeal;
import com.jack.reggie.entity.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithSetmeal(SetmealDto setmealDto);

    public void deleteWithSetmeal(List ids);
}
