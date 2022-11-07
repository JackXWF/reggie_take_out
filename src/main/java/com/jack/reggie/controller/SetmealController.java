package com.jack.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggie.common.R;
import com.jack.reggie.entity.Category;
import com.jack.reggie.entity.Setmeal;
import com.jack.reggie.entity.SetmealDto;
import com.jack.reggie.service.CategoryService;
import com.jack.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        log.info("套擦吧" + setmealDto);

        System.out.println("haha" + setmealDto.getPrice());

        setmealService.saveWithSetmeal(setmealDto);

        return R.success("新增套餐成功");
    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Setmeal> pageInfo = new Page(page, pageSize);
        Page<SetmealDto> pageDto = new Page();


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;

        }).collect(Collectors.toList());

        pageDto.setRecords(list);

        System.out.println(list + "菜菜");

        return R.success(pageDto);
    }


    /**
     * 删除套餐
     */
    @DeleteMapping
    public R<String>delete(@RequestParam List ids){

        setmealService.deleteWithSetmeal(ids);

        return R.success("删除成功");
    }

    /**
     * 修改套餐状态 停售
     */
    @PostMapping("/status/0")
    public R<String>status0(@RequestParam List ids){

        System.out.println("这是套餐的id="+ids);

        //update setmeal set status =1 where id= ids

        LambdaQueryWrapper<Setmeal>queryWrapper = new LambdaQueryWrapper();

        queryWrapper.in(Setmeal::getId,ids);
        //queryWrapper.eq(Setmeal::getId,ids);

        Setmeal setmeal=new Setmeal();

        setmeal.setStatus(0);

        setmealService.update(setmeal,queryWrapper);

        return R.success("停售成功");
    }

    /**
     * 修改套餐状态 停售
     */
    @PostMapping("/status/1")
    public R<String>status1(@RequestParam List ids){

        System.out.println("这是套餐的id="+ids);

        //update setmeal set status =1 where id= ids

        LambdaQueryWrapper<Setmeal>queryWrapper = new LambdaQueryWrapper();

        queryWrapper.in(Setmeal::getId,ids);
        //queryWrapper.eq(Setmeal::getId,ids);

        Setmeal setmeal=new Setmeal();

        setmeal.setStatus(1);

        setmealService.update(setmeal,queryWrapper);

        return R.success("启售成功");
    }


    /**
     * 套餐展示
     */
    @GetMapping("/list")
    public R<List<Setmeal>>list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }



}
