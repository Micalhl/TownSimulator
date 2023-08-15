package com.mcstarrysky.townsimulator.data

import com.mcstarrysky.townsimulator.data.entity.*
import com.mcstarrysky.townsimulator.data.mail.Mail
import com.mcstarrysky.townsimulator.prettyInfo
import com.mcstarrysky.townsimulator.data.request.Request
import com.mcstarrysky.townsimulator.data.town.Town
import com.mcstarrysky.townsimulator.data.vote.Vote
import org.bukkit.Bukkit
import taboolib.common.io.newFile
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.onlinePlayers
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.typeOf

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.Storage
 *
 * @author Mical
 * @since 2023/7/24 10:31
 */
object Storage {

    val players = ConcurrentHashMap<UUID, Human>()
    val towns = ConcurrentHashMap<UUID, Town>()
    val mails = ConcurrentHashMap<UUID, Mail>()
    val requests = ConcurrentHashMap<UUID, Request>()
    val votes = ConcurrentHashMap<UUID, Vote>() // TODO

    @Suppress("UNCHECKED_CAST")
    fun loadPlayers() {
        newFolder(getDataFolder(), "players").listFiles { file -> file.extension == "json" }?.forEach { file ->
            val config = Configuration.loadFromFile(file, Type.JSON)
            val clazz = Class.forName(config["type"]?.toString()) as Class<Human>
            val human = Configuration.deserialize(config, clazz.unsafeInstance() as Human, ignoreConstructor = true)
            players += human.uniqueId to human
        }
        onlinePlayers.filterNot { players.contains(it.uniqueId) }.forEach {
            players += it.uniqueId to Human(it.uniqueId)
        }
        prettyInfo("已加载 ${players.size} 个玩家数据")
    }

    fun loadMails() {
        newFolder(getDataFolder(), "mails").listFiles { file -> file.extension == "json" }?.forEach { file ->
            val config = Configuration.loadFromFile(file, Type.JSON)
            val mail = Configuration.deserialize<Mail>(config, ignoreConstructor = true)
            mails += mail.uniqueId to mail
        }
        prettyInfo("已加载 ${mails.size} 封邮件")
    }

    fun loadTowns() {
        newFolder(getDataFolder(), "towns").listFiles { file -> file.extension == "json" }?.forEach { file ->
            val config = Configuration.loadFromFile(file, Type.JSON)
            val town = Configuration.deserialize<Town>(config, ignoreConstructor = true)
            towns += town.uniqueId to town
        }
        mails.values.filter { Bukkit.getPlayer(it.player) != null }.forEach { it.trySend() }
        prettyInfo("已加载 ${towns.size} 个聚落数据")
    }

    @Suppress("UNCHECKED_CAST")
    fun loadRequests() {
        newFolder(getDataFolder(), "requests").listFiles { file -> file.extension == "json" }?.forEach { file ->
            val config = Configuration.loadFromFile(file, Type.JSON)
            val clazz = Class.forName(config["type"]?.toString()) as Class<Request>
            val request = Configuration.deserialize(config, clazz.unsafeInstance() as Request, ignoreConstructor = true)
            requests += request.uniqueId to request
        }
    }

    fun savePlayers() {
        prettyInfo("正在保存玩家数据...")
        for ((uniqueId, human) in players) {
            newFile(newFolder(getDataFolder(), "players"), "${uniqueId}.json")
                .writeText(Configuration.serialize(human, Type.JSON).also { it["type"] = human::class.java.name }.toString())
        }
    }

    fun saveMails() {
        prettyInfo("正在保存邮件数据...")
        for ((uniqueId, mail) in mails) {
            newFile(newFolder(getDataFolder(), "mails"), "${uniqueId}.json")
                .writeText(Configuration.serialize(mail, Type.JSON).toString())
        }
    }

    fun saveTowns() {
        prettyInfo("正在保存聚落数据...")
        newFolder(getDataFolder(), "towns")
            .listFiles { file -> file.extension == ".yml" }
            ?.toMutableList()
            ?.filterNot { it.nameWithoutExtension in towns.values.map { it.uniqueId.toString() } }
            ?.forEach { it.delete() }
        for ((uniqueId, town) in towns) {
            newFile(newFolder(getDataFolder(), "towns"), "${uniqueId}.json")
                .writeText(Configuration.serialize(town, Type.JSON).toString())
        }
    }

    fun saveRequests() {
        prettyInfo("正在保存申请数据...")
        for ((uniqueId, request) in requests) {
            newFile(newFolder(getDataFolder(), "requests"), "${uniqueId}.json")
                .writeText(Configuration.serialize(request, Type.JSON).also { it["type"] = request::class.java.name }.toString())
        }
    }
}

inline fun <reified T> data(uniqueId: UUID): T? {
    return when (typeOf<T>()) {
        typeOf<Human>(), typeOf<Member>(), typeOf<Admin>(), typeOf<Owner>() -> Storage.players[uniqueId] as? T
        typeOf<Town>() -> Storage.towns[uniqueId] as? T
        else -> null
    }
}