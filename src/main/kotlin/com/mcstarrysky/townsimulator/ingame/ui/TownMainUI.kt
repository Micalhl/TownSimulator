package com.mcstarrysky.townsimulator.ingame.ui

import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Human
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.data.town.TownHelper
import com.mcstarrysky.townsimulator.info
import org.bukkit.entity.Player
import org.serverct.parrot.parrotx.mechanism.Reloadable
import org.serverct.parrot.parrotx.ui.MenuComponent
import org.serverct.parrot.parrotx.ui.config.MenuConfiguration
import org.serverct.parrot.parrotx.ui.feature.util.MenuFunctionBuilder
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.nextChat

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.ingame.ui.TownMainUI
 *
 * @author Mical
 * @since 2023/7/24 18:45
 */
@MenuComponent("TownMainMenu")
object TownMainUI {

    @Config("ui/town/menu.yml")
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
        player.openMenu<Basic>(config.title().component().build { colored() }.toRawMessage()) {
            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            map(*shape.array)

            onBuild { _, inventory ->
                shape.all { slot, index, item, _ ->
                    inventory.setItem(slot, item(slot, index))
                }
            }

            onClick {
                it.isCancelled = true
                if (it.rawSlot in shape) {
                    templates[it.rawSlot]?.handle(this, it)
                }
            }
        }
    }

    @MenuComponent
    private val create = MenuFunctionBuilder {
        onClick { (_, _, _, e, _) ->
            e.clicker.closeInventory()
            if (!e.clicker.hasPermission("town.create")) return@onClick
            e.clicker.info("请输入你要创建的聚落的名称")
            e.clicker.nextChat {
                TownHelper.createTown(e.clicker, it)
            }
        }
    }

    @MenuComponent
    private val join = MenuFunctionBuilder {

    }

    @MenuComponent
    private val my = MenuFunctionBuilder {
        onClick { (_, _, _, e, _) ->
            e.clicker.closeInventory()
            val human = data<Human>(e.clicker.uniqueId)
            if (human !is Member) {
                e.clicker.info("你不处于一个聚落中, 无法为你打开聚落详情面板")
                return@onClick
            }
            TownControlUI.open(e.clicker)
        }
    }
}