package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.constant.ProductConstant;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        //1.保持当前表的基本数据
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.save(attrEntity);

        //2.保持关联关系
        //当前关系为基本类型且存在属性组id时才进行保存，避免在进行关联操作时属性及属性组关联表中出现同一属性多次保存的情况
        if(attr.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    /**
     * 插叙分类id{catelogId}包含属性集合
     * @param params
     * @param catelogId
     * @param type 属性类型 1.销售属性 2.类型属性
     * @return
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId,String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type","base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        //检查是否传输分类id
        if(catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        //获取搜索信息
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        //进行分页及条件查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        //得到符合条件的记录的所属分类名和所属分组名
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        //处理得到所需的前端页面所需vo对象集合
        List<AttrRespVo> respVos = records.stream().map(
                (attrEntity)-> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //1.设置分类和分组的名字
            AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationDao
                    .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrEntity.getAttrId()));
            //所属分组id存在，获取所属分组名
            if (attrId != null && attrId.getAttrGroupId()!=null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                if(attrGroupEntity!=null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            //所属分类id存在，则搜索所属分类名
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVo);

        //1.设置分组信息
        AttrAttrgroupRelationEntity attrgroupRelation = attrAttrgroupRelationDao.
                selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                        eq("attr_id",attrEntity.getAttrId()));
        if(attrgroupRelation!=null){
            respVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
            if(attrGroupEntity!=null){
                respVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        //2.设置分类信息
        Long cateLogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(cateLogId);
        respVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(cateLogId);
        if(categoryEntity!=null){
            respVo.setCatelogName(categoryEntity.getName());
        }

        return respVo;
    }

    @Override
    public void updateAttr(AttrVo attr) {
        //更新属性表
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        //修改分组关联
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();

        relationEntity.setAttrId(attr.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());

        //查找是否已经存在属性与属性组的关联
        Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        if(count>0){
            //存在关联，进行更细操作
            attrAttrgroupRelationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        }else{
            //不存在管理，直接插入新的关联记录
            attrAttrgroupRelationDao.insert(relationEntity);
        }

    }

    /**
     * 获取分组id{attrGroupId}关联的属性集合
     * @param attrGroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        //找到属性关联表中属性组id为attrGroupId的所有记录
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao
                .selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));

        //获取这些记录的所有属性id
        List<Long> attrIds = entities.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());

        //属性组关联的属性可能不存在，需要判空
        if(attrIds==null || attrIds.size()==0){
            return null;
        }

        //在属性表查找中查找这些属性id返回集合
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);

        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 删除vos内对应属性及属性组关系映射
     * @param vos
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //将vos内元素转化为attrGroupRelationEntity
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        //进行批量删除
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
     * 获取分组id{attrGroupId}可以进行关联的分组信息
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelation(Map<String, Object> params, Long attrgroupId) {
        //1.当前分组只能关联自己所属的分类里所有的属性
            //获取当前分组attrgroupId所属分类的id
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
            Long catelogId = attrGroupEntity.getCatelogId();

        //2.当前分组只能关联别的分组没有引用的属性
            //2.1 当前分类下的其他分组
            List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
            //提取出这戏记录的分组id
            List<Long> collect = group.stream().map(item -> {
                return item.getAttrGroupId();
            }).collect(Collectors.toList());

            //2.2 这些分组关联的属性
            List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .in("attr_group_id", collect));
            //提取出对应属性id集合
            List<Long> attrIds = groupId.stream().map(item -> {
                return item.getAttrId();
            }).collect(Collectors.toList());

            //2.3 从当前分类的所有属性中移除这些属性
            QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
            if(attrIds!=null && attrIds.size()>0){
                wrapper.notIn("attr_id",attrIds);
            }

            //获取搜索条件
            String key = (String) params.get("key");
            if(!StringUtils.isEmpty(key)){
                wrapper.and(wp->{
                    wp.eq("attr_id",key).or().like("attr_name",key);
                });
            }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),wrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;

    }


}