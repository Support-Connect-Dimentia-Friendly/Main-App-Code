package com.bysj.alzheimer.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:wangjianlin
 * @Description:
 * @Date:Created in 14:19 2019/9/12
 * @Modified By:
 */
@Configuration
@MapperScan("com.bysj.alzheimer.mapper")
public class MybatisPlusConfigurer {

}
