package com.gali.rpc.service;

import io.vertx.core.Vertx;

/**
 * VertxHttpServer:
 *
 * @author gali
 * @date 2024/06/17
 */
public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        //创建实例
        Vertx vertx = Vertx.vertx();

        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //监听端口并处理
        httpServer.requestHandler(request -> {

            request.response()
                    .putHeader("Content-Type", "text/plain")
                    .end("Hello");
        });

        //启动并监听端口
        httpServer.listen(port, res -> {
            if (res.succeeded()) {
                System.out.println("success,port" + port);
            } else {
                System.out.println("error,port" + port);
            }
        });


    }


    public static void main(String[] args) {
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8080);
    }
}
