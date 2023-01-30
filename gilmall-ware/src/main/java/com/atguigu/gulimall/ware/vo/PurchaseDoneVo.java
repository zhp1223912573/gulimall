package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zhp
 * @date 2023-01-30 14:15
 * 完成采购前端请求参数
 */
@Data
public class PurchaseDoneVo {
    /**
     * 采购单id
     */
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
