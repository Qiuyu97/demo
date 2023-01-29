package com.qiuyu.demo.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @Description: Result
 * @Author: qiuyu
 * @Date: 2022/4/25
 **/
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result<T> {
    private Integer status;

    private String message;

    private T data;

    public Result(ResultEnum resultEnum) {
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
    }

    public Result(ResultEnum resultEnum, T t) {
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
        this.data = t;
    }

    public Result ok() {
        return new Result(ResultEnum.OK);
    }

    public Result<T> ok(T t) {
        return new Result<>(ResultEnum.OK, t);
    }

}
