package com.mcstarrysky.townsimulator.data

import taboolib.common.platform.Schedule

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.Tasks
 *
 * @author mical
 * @since 2023/8/10 1:44 PM
 */
object Tasks {

    @Schedule(period = 20L, async = true)
    fun tickVotes() {
        Storage.votes.values.forEach {
            if (System.currentTimeMillis() >= it.timestamp && !it.isDeprecated) {
                it.stop()
            }
        }
    }

    @Schedule
    fun tickPlayers() {

    }
}