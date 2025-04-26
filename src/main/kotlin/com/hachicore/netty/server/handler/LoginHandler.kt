package com.hachicore.netty.server.handler

import com.hachicore.netty.domain.ChannelRepository
import com.hachicore.netty.domain.User
import io.netty.channel.ChannelHandler.Sharable

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Sharable
class LoginHandler(
    private val channelRepository: ChannelRepository
) : ChannelInboundHandlerAdapter() {

    private val log: Logger = LoggerFactory.getLogger(LoginHandler::class.java)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is String || !msg.startsWith("login ")) {
            ctx.fireChannelRead(msg)
            return
        }

        if (log.isDebugEnabled) {
            log.debug(msg.toString())
        }

        val user: User = User.of(msg, ctx.channel())
        user.login(channelRepository, ctx.channel())

        ctx.writeAndFlush("Successfully logged in as ${user.username}" + ". \r\n")
    }

}