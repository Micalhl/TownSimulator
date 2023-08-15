package com.mcstarrysky.townsimulator.command.sub

import com.mcstarrysky.townsimulator.command.CommandExecutor
import com.mcstarrysky.townsimulator.command.CommandHandler
import com.mcstarrysky.townsimulator.data.town.TownHelper
import org.bukkit.entity.Player
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.command.sub.CommandCreate
 *
 * @author Mical
 * @since 2023/7/24 18:03
 */
object CommandCreate : CommandExecutor {

    override val command: SimpleCommandBody
        get() = subCommand {
            dynamic("name") {
                execute<Player> { sender, _, argument ->
                    TownHelper.createTown(sender, argument)
                }
            }
        }

    override val name: String
        get() = "create"

    override val description: String
        get() = "创建聚落"

    override val usage: String
        get() = "§7[§8聚落名称§7]"

    init {
        CommandHandler.sub[name] = this
    }
}