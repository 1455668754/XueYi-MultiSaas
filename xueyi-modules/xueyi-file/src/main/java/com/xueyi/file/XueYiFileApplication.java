package com.xueyi.file;

import com.xueyi.common.security.annotation.EnableRyFeignClients;
import com.xueyi.common.security.config.ApplicationConfig;
import com.xueyi.common.security.config.JacksonConfig;
import com.xueyi.common.security.feign.FeignAutoConfiguration;
import com.xueyi.common.swagger.annotation.EnableCustomSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 文件服务
 *
 * @author xueyi
 */
@EnableCustomSwagger
@EnableRyFeignClients
@Import({ApplicationConfig.class, FeignAutoConfiguration.class, JacksonConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class XueYiFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(XueYiFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
