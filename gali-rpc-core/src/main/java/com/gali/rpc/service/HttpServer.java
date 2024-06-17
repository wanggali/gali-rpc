package com.gali.rpc.service;

/**
 * HttpServer:
 *
 * @author gali
 * @date 2024/06/17
 */
public interface HttpServer {
    /**
     * 启动服务器
     * @param port
     */
    void  doStart(int port);
}
