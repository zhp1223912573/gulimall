package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhp
 * @date 2023-01-12 1:29
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {


    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
