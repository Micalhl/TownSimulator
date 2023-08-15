package com.mcstarrysky.townsimulator.ingame.ui

import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.emptyItemStack
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.frameItemStack
import com.mcstarrysky.townsimulator.data.request.JoinRequest
import com.mcstarrysky.townsimulator.data.request.QuitRequest
import com.mcstarrysky.townsimulator.data.town.Town
import org.bukkit.entity.Player
import org.serverct.parrot.parrotx.function.singleton
import org.serverct.parrot.parrotx.function.singletons
import org.serverct.parrot.parrotx.mechanism.Reloadable
import org.serverct.parrot.parrotx.ui.MenuComponent
import org.serverct.parrot.parrotx.ui.config.MenuConfiguration
import org.serverct.parrot.parrotx.ui.feature.util.MenuFunctionBuilder
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Basic

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.ingame.ui.TownControlUI
 *
 * @author Mical
 * @since 2023/7/25 00:25
 */
@MenuComponent("TownControl")
object TownControlUI {

    @Config("ui/town/info.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    @Reloadable
    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        if (!::config.isInitialized) {
            config = MenuConfiguration(source)
        }
        val member = data<Member>(player.uniqueId) ?: return
        val town = member.getTown()
        player.openMenu<Basic>(config.title("name" to { town.name }).component().build { colored() }.toRawMessage()) {
            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            map(*shape.array)

            onBuild { _, inventory ->
                shape.all { slot, index, item, _ ->
                    inventory.setItem(slot, item(slot, index) {
                        this += "town" to town
                        this += "player" to player
                    })
                }
            }

            onClick {
                it.isCancelled = true
                if (it.rawSlot in shape) {
                    templates[it.rawSlot]?.handle(this, it) {
                        this += "town" to town
                    }
                }
            }
        }
    }

    @MenuComponent
    private val joinRequest = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val town = args["town"] as? Town ?: return@onBuild emptyItemStack.clone()
            val player = args["player"] as? Player ?: return@onBuild emptyItemStack.clone()
            val requests = town.getRequests().filterIsInstance<JoinRequest>()
            return@onBuild if (requests.isEmpty())
                frameItemStack.clone()
            else if (town.getOwner().uniqueId != player.uniqueId)
                frameItemStack.clone()
            else
                icon.singleton("requests", requests.size.toString())
        }
        onClick { (_, _, _, event, args) ->
            val town = args["town"] as? Town ?: return@onClick
            if (town.getOwner().uniqueId != event.clicker.uniqueId) return@onClick
            if (town.getRequests().filterIsInstance<JoinRequest>().isEmpty()) return@onClick
            // TODO
        }
    }

    @MenuComponent
    private val quitRequest = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val town = args["town"] as? Town ?: return@onBuild emptyItemStack.clone()
            val player = args["player"] as? Player ?: return@onBuild emptyItemStack.clone()
            val requests = town.getRequests().filterIsInstance<QuitRequest>()
            if (requests.isEmpty())
                frameItemStack.clone()
            else if (town.getOwner().uniqueId != player.uniqueId)
                frameItemStack.clone()
            else
                icon.singleton("requests", requests.size.toString())
        }
        onClick { (_, _, _, event, args) ->
            val town = args["town"] as? Town ?: return@onClick
            if (town.getOwner().uniqueId != event.clicker.uniqueId) return@onClick
            if (town.getRequests().filterIsInstance<QuitRequest>().isEmpty()) return@onClick
            // TODO
        }
    }

    @MenuComponent
    private val control = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val town = args["town"] as? Town ?: return@onBuild emptyItemStack.clone()
            val player = args["player"] as? Player ?: return@onBuild emptyItemStack.clone()
            val member = data<Member>(player.uniqueId) ?: return@onBuild emptyItemStack.clone()
            icon.singletons {
                when (it) {
                    "name" -> town.name
                    "create" -> town.getTime()
                    "role" -> member.getRoleName()
                    "date" -> member.getTime()
                    else -> ""
                }
            }
        }
    }
}