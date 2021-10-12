/*
 * @Date: 2021-09-09 11:58:33
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 16:41:23
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\StzbApplication.java
 */
package com.kaoqin.stzb;
import com.kaoqin.stzb.entity.Constant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@MapperScan("com.kaoqin.stzb.dao")
@EnableConfigurationProperties(Constant.class)
public class StzbApplication {

	public static void main(String[] args) { 
		SpringApplication.run(StzbApplication.class, args);
	}

}
