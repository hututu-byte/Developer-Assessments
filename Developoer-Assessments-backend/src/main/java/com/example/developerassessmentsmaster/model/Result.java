package com.example.developerassessmentsmaster.model;

import lombok.Data;
import lombok.var;

/**
 * 通用返回结果类,服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
//千万不要加@Schema(name = "通用返回结果")，会导致web端api文档不识别模型
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    /**
     * 成功回调
     *
     * @param data 数据体
     * @param <T>  回调类型
     * @return 成功回调数据
     */
    public static <T> Result<T> success(T data) {
        var result = new Result<T>();
        result.data = data;
        result.msg = "success";
        result.code = 200;
        return result;
    }

    /**
     * 成功回调（无数据）
     */
    public static <T> Result<T> success() {
        var result = new Result<T>();
        result.data = null;
        result.msg = "success";
        result.code = 200;
        return result;
    }

    /**
     * 错误回调
     *
     * @param msg 错误信息
     * @param <T> 空回调类型
     * @return 空回调
     */
    public static <T> Result<T> error(int code, String msg) {
        var result = new Result<T>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    /**
     * 自定义回调
     *
     * @param code 状态码
     * @param msg  回调信息
     * @param data 数据体
     * @param <T>  回调类型
     * @return 回调数据
     */
    public static <T> Result<T> normal(int code, String msg, T data) {
        var result = new Result<T>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    /**
     * 错误回调
     *
     * @param msg 错误信息
     * @param <T> 空回调类型
     * @return 空回调
     */
    public static <T> Result<T> error(String msg) {
        var result = new Result<T>();
        result.msg = msg;
        return result;
    }
}
