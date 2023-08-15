package com.mcstarrysky.townsimulator.command.sub

import com.mcstarrysky.townsimulator.command.CommandExecutor
import com.mcstarrysky.townsimulator.command.CommandHandler
import com.mcstarrysky.townsimulator.ingame.ui.TownMainUI
import org.bukkit.entity.Player
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.command.sub.CommandOpen
 *
 * @author Mical
 * @since 2023/7/24 19:09
 */
object CommandOpen : CommandExecutor {

    override val command: SimpleCommandBody
        get() = subCommand {
            execute<Player> { sender, _, _ ->
                TownMainUI.open(sender)
            }
        }

    override val name: String
        get() = "open"

    override val description: String
        get() = "打开聚落面板"

    override val usage: String
        get() = ""

    init {
        CommandHandler.sub[name] = this
    }
}