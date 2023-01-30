package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @author zhp
 * @date 2023-01-30 14:16
 */
@Data
public class PurchaseItemDoneVo {
    /**
     * 采购项id
     */
    private Long itemId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 完成/失败的需求详情
     */
    private String reason;
}
