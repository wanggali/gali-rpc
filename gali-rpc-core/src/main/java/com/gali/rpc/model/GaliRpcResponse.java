package com.gali.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * GaliRpcResponse:
 *
 * @author gali
 * @date 2024/06/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GaliRpcResponse implements Serializable {
    /**
     * 相应数据
     */
    private Object data;

    /**
     * 相应数据类型
     */
    private Class<?> dataType;

    /**
     * 相应信息
     */
    private String message;

    /**
     * 异常信息
     */
    private Exception exception;
}
