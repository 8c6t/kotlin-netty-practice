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
class SimpleChatServerHandler(
    private val channelRepository: ChannelRepository
): ChannelInboundHandlerAdapter() {

    private val log: Logger = LoggerFactory.getLogger(SimpleChatServerHandler::class.java)

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.fireChannelActive()

        if (log.isDebugEnabled) {
            log.debug("{}", ctx.channel().remoteAddress())
        }

        val remoteAddress = ctx.channel().remoteAddress().toString()
        ctx.writeAndFlush("Your remote address is $remoteAddress.\r\n")

        if (log.isDebugEnabled) {
            log.debug("Bound Channel Count is {}", channelRepository.size())
        }
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        var stringMsg = msg as String
        if (log.isDebugEnabled) {
            log.debug(stringMsg)
        }

        if (stringMsg.startsWith("login ")) {
            ctx.fireChannelRead(msg)
            return
        }

        val splitMessage: Array<String> = stringMsg.split("::").toTypedArray()

        if (splitMessage.size != 2) {
            ctx.channel().writeAndFlush(stringMsg + "\n\r")
            return
        }

        User.current(ctx.channel())
            .tell(channelRepository.get(splitMessage[0]), splitMessage[0], splitMessage[1])
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error(cause.message, cause)
    }

    // todo 연결이 정상적으로 종료되지 않았을 때 처리?
    override fun channelInactive(ctx: ChannelHandlerContext) {
        User.current(ctx.channel()).logout(channelRepository, ctx.channel())
        if (log.isDebugEnabled) {
            log.debug("Channel Count is {}", this.channelRepository.size())
        }
    }

}
