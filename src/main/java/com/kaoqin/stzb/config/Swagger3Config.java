/*
 * @Date: 2021-10-12 17:11:50
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-13 16:59:13
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\config\Swagger3Config.java
 */
package com.kaoqin.stzb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import org.springframework.http.HttpMethod;
import io.swagger.v3.oas.annotations.Operation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
public class Swagger3Config implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        //返回文档摘要信息
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()

                .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                .paths(PathSelectors.any())
                .build()
                // .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET, getGlobalResonseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResonseMessage());
    }

    //生成接口信息，包括标题、联系人等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("韶华考勤接口文档")
                .description("如有疑问，请联系开发老皮卡")
                .contact(new Contact("老皮卡", "https://github.com/chinnseii/shaohua", "csw83733630@icloud.com"))
                .version("1.0")
                .build();
    }

    //生成全局通用参数
    // private List<RequestParameter> getGlobalRequestParameters() {
    //     List<RequestParameter> parameters = new ArrayList<>();
    //     parameters.add(new RequestParameterBuilder()
    //             .name("appid")
    //             .description("平台id")
    //             .required(true)
    //             .in(ParameterType.QUERY)
    //             .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
    //             .required(false)
    //             .build());
    //     parameters.add(new RequestParameterBuilder()
    //             .name("udid")
    //             .description("设备的唯一id")
    //             .required(true)
    //             .in(ParameterType.QUERY)
    //             .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
    //             .required(false)
    //             .build());
    //     parameters.add(new RequestParameterBuilder()
    //             .name("version")
    //             .description("客户端的版本号")
    //             .required(true)
    //             .in(ParameterType.QUERY)
    //             .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
    //             .required(false)
    //             .build());
    //      return parameters;
    // }

    //生成通用响应信息
    private List<Response> getGlobalResonseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
         return responseList;
    }
}
