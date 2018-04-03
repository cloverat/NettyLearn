package com.cloverat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenyujun
 * @date 18-4-3
 */
@SpringBootApplication
@EnableScheduling
public class NettyLearnApplication {

    // 采用springBoot启动服务，启动netty
    public static void main(String[] args) {
        SpringApplication.run(NettyLearnApplication.class, args);
        System.out.println("启动成功");
    }
}
