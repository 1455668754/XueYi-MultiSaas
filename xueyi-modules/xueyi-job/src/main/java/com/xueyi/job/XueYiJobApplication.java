package com.xueyi.job;

import com.xueyi.common.security.annotation.EnableCustomConfig;
import com.xueyi.common.security.annotation.EnableResourceServer;
import com.xueyi.common.security.annotation.EnableRyFeignClients;
import com.xueyi.common.swagger.annotation.EnableCustomSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 定时任务
 *
 * @author xueyi
 */
@EnableCustomConfig
@EnableCustomSwagger
@EnableResourceServer
@EnableRyFeignClients
@SpringBootApplication
public class XueYiJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(XueYiJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  _____     __   ____     __        \n" +
                "  \\   _\\   /  /  \\   \\   /  /   \n" +
                "  .-./ ). /  '    \\  _. /  '       \n" +
                "  \\ '_ .') .'      _( )_ .'        \n" +
                " (_ (_) _) '   ___(_ o _)'          \n" +
                "   /    \\   \\ |   |(_,_)'         \n" +
                "   `-'`-'    \\|   `-'  /           \n" +
                "  /  /   \\    \\\\      /          \n" +
                " '--'     '----'`-..-'              ");
    }
}
