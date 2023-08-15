package com.mcstarrysky.townsimulator.ingame.listener

import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.emptyLocation
import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.component
import taboolib.platform.compat.replacePlaceholder

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.ingame.listener.ListenerPlayerMove
 *
 * @author mical
 * @since 2023/8/10 3:14 PM
 */
object ListenerPlayerMove {

    @SubscribeEvent
    fun e(e: PlayerMoveEvent) {
        Storage.towns.values.filter { town ->
            town.getRegions().any { it.checkIn(e.to ?: emptyLocation) } && town.getRegions().none {
                it.checkIn(e.from)
            }
        }.forEach { town ->
            // 进入区域
            town.enterMessage.replacePlaceholder(e.player).component().buildColored().sendTo(adaptPlayer(e.player))
        }

        Storage.towns.values.filter { town ->
            town.getRegions().any { it.checkIn(e.from) } && town.getRegions().none {
                it.checkIn(e.to ?: emptyLocation)
            }
        }.forEach { town ->
            // 离开区域
            town.leaveMessage.replacePlaceholder(e.player).component().buildColored().sendTo(adaptPlayer(e.player))
        }
    }
}