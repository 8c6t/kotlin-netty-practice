package com.hachicore.netty.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import jakarta.annotation.PreDestroy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.InetSocketAddress

@Component
class NettyServer(
    private val serverBootstrap: ServerBootstrap,
    private val socketAddress: InetSocketAddress,
    private var serverChannel: Channel?
) {

    private val log: Logger = LoggerFactory.getLogger(NettyServer::class.java)

    fun start() {
        try {
            val serverChannelFuture = serverBootstrap.bind(socketAddress).sync()
            log.info("Server start. Host: {}, Port: {}", socketAddress.hostName, socketAddress.port)
            serverChannel = serverChannelFuture.channel().closeFuture().sync().channel()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    @PreDestroy
    fun stop() {
        serverChannel?.let { it
            it.close()
            it.parent().closeFuture()
        }
    }

}