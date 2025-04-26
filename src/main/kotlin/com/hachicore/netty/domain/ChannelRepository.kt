package com.hachicore.netty.domain

import io.netty.channel.Channel
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class ChannelRepository(
    private val channelCache: ConcurrentMap<String, Channel> = ConcurrentHashMap()
) {

    fun put(key: String, value: Channel) {
        channelCache[key] = value
    }

    fun get(key: String): Channel? {
        return channelCache[key]
    }

    fun remove(key: String) {
        channelCache.remove(key)
    }

    fun size(): Int {
        return channelCache.size
    }

}