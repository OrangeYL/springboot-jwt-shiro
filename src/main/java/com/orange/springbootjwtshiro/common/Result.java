package com.orange.springbootjwtshiro.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一通用结果返回类
 *
 * @author:Li ZhiCheng
 * @create:2022-08-2022/8/10 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {

    static final long serialVersionUID = 1L;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }
    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static Result success(int code, String message) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result success(int code, String message, Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage("操作失败");
        return result;
    }

    public static Result fail(Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage("操作失败");
        result.setData(data);
        return result;
    }

    public static Result fail(int code, String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result fail(int code, String message, Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

}
