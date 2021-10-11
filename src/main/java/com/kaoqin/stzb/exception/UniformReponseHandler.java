/*
 * @Date: 2021-10-06 18:30:32
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-06 21:44:53
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\exception\UniformReponseHandler.java
 */
package com.kaoqin.stzb.exception;



import com.kaoqin.stzb.entity.CallResultMsg;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class UniformReponseHandler<T> {

    @ResponseStatus(HttpStatus.OK)
    public CallResultMsg<T> sendSuccessResponse(){
        return new CallResultMsg<>();
    }

    @ResponseStatus(HttpStatus.OK)
    public CallResultMsg<T> sendSuccessResponse(T data) {
        return new CallResultMsg<>(data);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CallResultMsg<T> sendErrorResponse_System(Exception exception){
        if (exception instanceof UserDefinedException) {
            return this.sendErrorResponse_UserDefined(exception);
        }
        return new CallResultMsg<>(CodeAndMsg.UNKNOWEXCEPTION);
    }

    @ExceptionHandler(UserDefinedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CallResultMsg<T> sendErrorResponse_UserDefined(Exception exception){
        return new CallResultMsg<>(((UserDefinedException)exception).getException());
    }

}