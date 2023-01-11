package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author zhp
 * @email 1223912573@qq.com
 * @date 2023-01-11 23:46:01
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
