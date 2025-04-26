package com.hachicore.netty

import com.hachicore.netty.config.NettyProperties
import com.hachicore.netty.server.NettyServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
@EnableConfigurationProperties(NettyProperties::class)
class NettyApplication(
	private val nettyServer: NettyServer
) {

	@EventListener(ApplicationReadyEvent::class)
	fun start() {
		nettyServer.start()
	}

}

fun main(args: Array<String>) {
	runApplication<NettyApplication>(*args)
}