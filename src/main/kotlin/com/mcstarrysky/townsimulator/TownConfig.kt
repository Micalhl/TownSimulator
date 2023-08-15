package com.mcstarrysky.townsimulator

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.TownConfig
 *
 * @author Mical
 * @since 2023/7/24 23:27
 */
object TownConfig {

    @Config
    lateinit var config: Configuration
        private set

    val world: String
        get() = config.getString("world") ?: "Space"
}