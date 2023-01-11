package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhp
 * @email 1223912573@qq.com
 * @date 2023-01-11 23:34:11
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
