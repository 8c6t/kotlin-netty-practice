package com.hachicore.netty.server

import com.hachicore.netty.server.handler.LoginHandler
import com.hachicore.netty.server.handler.SimpleChatServerHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import org.springframework.stereotype.Component

@Component
class SimpleChatChannelInitializer(
    private val simpleChatServerHandler: SimpleChatServerHandler,
    private val loginHandler: LoginHandler,
    private val stringEncoder: StringEncoder = StringEncoder(),
    private val stringDecoder: StringDecoder = StringDecoder()
): ChannelInitializer<SocketChannel>(){

    override fun initChannel(channel: SocketChannel) {
        val pipeline = channel.pipeline()

        pipeline.addLast(DelimiterBasedFrameDecoder(1024 * 1024, *Delimiters.lineDelimiter()))

        pipeline.addLast(stringDecoder)
        pipeline.addLast(stringEncoder)
        pipeline.addLast(simpleChatServerHandler)
        pipeline.addLast(loginHandler)
    }

}