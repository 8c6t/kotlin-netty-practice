package com.hachicore.netty.domain

import io.netty.channel.Channel
import io.netty.util.AttributeKey

class User(
    val username: String,
    val channel: Channel
) {

    companion object {
        val USER_ATTRIBUTE_KEY: AttributeKey<User> = AttributeKey.newInstance("USER")

        fun of(loginCommand: String, channel: Channel): User {
            if (loginCommand.startsWith("login ")) {
                return User(loginCommand.trim().substring("login ".length), channel)
            }
            throw IllegalArgumentException("loginCommand [$loginCommand] can not be accepted")
        }

        fun current(channel: Channel): User {
            val user = channel.attr(USER_ATTRIBUTE_KEY).get() ?: throw UserLoggedOutException()
            return user
        }
    }

    fun login(channelRepository: ChannelRepository, channel: Channel) {
        channel.attr(USER_ATTRIBUTE_KEY).set(this)
        channelRepository.put(this.username, channel)
    }

    fun logout(channelRepository: ChannelRepository, channel: Channel) {
        channel.attr(USER_ATTRIBUTE_KEY).getAndSet(null)
        channelRepository.remove(this.username)
    }

    fun tell(targetChannel: Channel?, username: String, message: String) {
        if (targetChannel != null) {
            targetChannel.write(this.username)
            targetChannel.write("> ")
            targetChannel.writeAndFlush(message + "\n\r")
            this.channel.writeAndFlush("The message was sent to [$username] successfully.\r\n")
        } else {
            this.channel.writeAndFlush("No user named with [$username].\r\n")
        }
    }

}


