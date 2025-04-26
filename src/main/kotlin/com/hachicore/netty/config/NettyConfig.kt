package com.hachicore.netty.config

import com.hachicore.netty.server.SimpleChatChannelInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress

@Configuration
class NettyConfig(
    private val nettyProperties: NettyProperties,
    private val simpleChatChannelInitializer: SimpleChatChannelInitializer
) {

    @Bean
    fun serverBootStrap(): ServerBootstrap {
        val bootstrap = ServerBootstrap()
        bootstrap.group(bossGroup(), workerGroup())
            .channel(NioServerSocketChannel::class.java)
            .handler(LoggingHandler(LogLevel.DEBUG))
            .childHandler(simpleChatChannelInitializer)

        bootstrap.option(ChannelOption.SO_BACKLOG, nettyProperties.backlog)

        return bootstrap
    }

    @Bean(destroyMethod = "shutdownGracefully")
    fun bossGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.bossCount)
    }

    @Bean(destroyMethod = "shutdownGracefully")
    fun workerGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.workerCount)
    }

    @Bean
    fun inetSocketAddress(): InetSocketAddress {
        return InetSocketAddress(nettyProperties.host, nettyProperties.port)
    }

}