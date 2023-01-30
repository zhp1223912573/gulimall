package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zhp
 * @date 2023-01-29 22:13
 * 合并采购需求前端请求参数
 */
@Data
public class MergeVo {
    /**
     * 合并的采购订单id
     */
    private Long purchaseId;

    /**
     * 被合并的采购需求id集合
     */
    private List<Long> items;
}
