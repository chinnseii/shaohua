/*
 * @Date: 2021-10-06 18:33:01
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-07 09:22:33
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\exception\UserDefinedException.java
 */
package com.kaoqin.stzb.exception;

public class UserDefinedException extends RuntimeException {
    private CodeAndMsg codeAndMsg;

    public UserDefinedException(CodeAndMsg exception){
        this.codeAndMsg = exception;
    }

    public CodeAndMsg getException() {
        return codeAndMsg;
    }

    public void setException(CodeAndMsg exception) {
        this.codeAndMsg = exception;
    }
}