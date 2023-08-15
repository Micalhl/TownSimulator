package com.mcstarrysky.townsimulator.ingame.listener

import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Human
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.ingame.listener.ListenerPlayerJoin
 *
 * @author Mical
 * @since 2023/7/24 15:28
 */
object ListenerPlayerJoin {

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (data<Human>(e.player.uniqueId) == null) {
            Storage.players += e.player.uniqueId to Human(e.player.uniqueId)
        }
        Storage.mails.values.filter { it.player == e.player.uniqueId }.forEach { it.trySend() }
    }
}