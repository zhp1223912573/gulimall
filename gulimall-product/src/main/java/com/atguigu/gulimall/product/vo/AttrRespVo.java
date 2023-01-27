package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @author zhp
 * @date 2023-01-27 0:18
 */
@Data
public class AttrRespVo extends AttrVo {
    //所属分类名
    private String catelogName;

    //所属分组名
    private String groupName;

    //所属分组完整路径
    private Long[] catelogPath;
}
