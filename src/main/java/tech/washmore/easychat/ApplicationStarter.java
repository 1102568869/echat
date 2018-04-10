package tech.washmore.easychat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Washmore
 * @version V1.0
 * @summary Family程序入口
 * @Copyright (c) 2017, www.washmore.tech
 * @since 2017/6/13
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("tech.washmore.easychat.dao")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationStarter {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ApplicationStarter.class, args);
            System.out.println("---------------正常启动--^_^------------#####################################");
        } catch (Exception e) {
            System.out.println("---------------启动挂了--T_T------------#####################################");
        }

    }
}
