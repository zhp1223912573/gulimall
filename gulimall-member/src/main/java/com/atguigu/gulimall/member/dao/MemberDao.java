package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhp
 * @email 1223912573@qq.com
 * @date 2023-01-11 23:17:55
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
