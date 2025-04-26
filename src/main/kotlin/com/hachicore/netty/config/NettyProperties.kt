package com.hachicore.netty.config

import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Range
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "server.netty")
data class NettyProperties (
    val host: String,
    @field:Range(min = 1000, max = 65536) val port: Int,
    @field:Min(1) val bossCount: Int,
    @field:Min(2) val workerCount: Int,
    val keepAlive: Boolean,
    val backlog: Int
)
