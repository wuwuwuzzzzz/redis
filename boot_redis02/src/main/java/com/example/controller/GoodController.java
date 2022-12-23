package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 *
 * @author wxz
 * @date 14:40 2022/12/23
 */
@RestController
public class GoodController {

    public static final String REDIS_LOCK = "wxzLock";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 
     * @author wxz
     * @date 14:53 2022/12/23
     * @return java.lang.String
     */
    @GetMapping("/buyGoods")
    public String buyGoods() {

        String value = UUID.randomUUID() + Thread.currentThread().getName();
        try {
            // setNX
            Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);

            if (Boolean.FALSE.equals(flag)) {
                return "抢锁失败!";
            }

            String s = stringRedisTemplate.opsForValue().get("goods:001");
            int goodNumber = s == null ? 0 : Integer.parseInt(s);

            if (goodNumber > 0) {
                int realNumber = goodNumber - 1;
                stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(realNumber));
                System.out.println("成功买到商品，库存还剩下" + realNumber + "件" + "\t 服务端口 " + serverPort);
                return "成功买到商品，库存还剩下" + realNumber + "件" + "\t 服务端口 " + serverPort;
            } else {
                System.out.println("商品已经售完/活动结束/调用超时，欢迎下次光临" + "\t 服务端口 " + serverPort);
                return "商品已经售完/活动结束/调用超时，欢迎下次光临" + "\t 服务端口 " + serverPort;
            }
        } finally {
            stringRedisTemplate.delete(REDIS_LOCK);
        }
    }
}
