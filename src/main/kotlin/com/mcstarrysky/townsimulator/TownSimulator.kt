package com.mcstarrysky.townsimulator

import com.mcstarrysky.townsimulator.data.Storage
import taboolib.common.platform.Plugin

object TownSimulator : Plugin() {

    override fun onEnable() {
        Storage.loadPlayers()
        Storage.loadTowns()
        Storage.loadMails()
        Storage.loadRequests()
        prettyInfo("已成功加载插件.")
    }

    override fun onDisable() {
        Storage.savePlayers()
        Storage.saveTowns()
        Storage.saveMails()
        Storage.saveRequests()
    }
}