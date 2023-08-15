package com.mcstarrysky.townsimulator

import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.data.mail.Mail
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.common.util.replaceWithOrder
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.ComponentText
import taboolib.module.chat.component
import taboolib.module.lang.asLangText
import taboolib.platform.util.asLangText
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.Utils
 *
 * @author Mical
 * @since 2023/7/24 00:40
 */
val emptyItemStack: ItemStack by unsafeLazy {
    ItemStack(Material.AIR, 1)
}

val frameItemStack: ItemStack by unsafeLazy {
    ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1)
}

val emptyLocation: Location by unsafeLazy {
    Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0)
}

fun ProxyCommandSender.prettyInfo(message: String, vararg args: Any) {
    sendMessage("${asLangText("prefix")} §7" + message.replaceWithOrder(*args))
}

fun prettyInfo(message: String, vararg args: Any) {
    console().prettyInfo(message, args)
}

fun OfflinePlayer.info(message: String, vararg args: Any) {
    val msg = ComponentText.of("${player!!.asLangText("prefix")} &{#C8A384}").append(message.replaceWithOrder(*args).component().build { colored() })
    if (isOnline) {
        msg.sendTo(adaptPlayer(player!!))
    } else {
        val mailUUID = UUID.randomUUID()
        Storage.mails += mailUUID to Mail(mailUUID, uniqueId, msg.toRawMessage())
    }
}

fun Chunk.getCircle(radius: Int): List<Chunk> {
    val chunks = mutableListOf<Chunk>()
    for (xx in x - radius..x + radius) {
        val interval = radius - abs(xx - x)
        for (zz in z - interval + 1 until z + interval) {
            if ((xx.toDouble() - x).
                pow(2) + (zz.toDouble() - z).pow(2) <= radius)
                chunks.add(world.getChunkAt(xx, zz))
        }
    }
    return chunks
}

fun getSurroundings(chunks: List<Chunk>): List<Chunk> {
    val world = chunks[0].world
    val graph = mutableMapOf<Chunk, Vector<Chunk>>()
    chunks.forEach { chunk ->
        val x = chunk.x
        val z = chunk.z
        listOf(
            world.getChunkAt(x - 1, z),
            world.getChunkAt(x, z + 1),
            world.getChunkAt(x + 1, z),
            world.getChunkAt(x, z - 1)
        ).forEach { nearBy ->
            graph.putIfAbsent(nearBy, Vector())
            graph[nearBy]!! += chunk
        }
    }
    return chunks.filter { chunk ->
        graph[chunk]!!.size < 4
    }
}

fun generateEdges(chunks: List<Chunk>, y: Double): List<Pair<Chunk, Chunk>> {
    if (chunks.isEmpty()) throw Exception()
    val graph = mutableMapOf<Chunk, Vector<Chunk>>()

    //生成一个单环连通双向图
    chunks.forEach { chunk ->
        val world = chunk.world
        val x = chunk.x
        val z = chunk.z
        graph.putIfAbsent(chunk, Vector())
        val nearbyChunks = listOf(
            world.getChunkAt(x - 1, z),
            world.getChunkAt(x, z + 1),
            world.getChunkAt(x + 1, z),
            world.getChunkAt(x, z - 1)
        ).filter { chunks.contains(it) }.forEach { nearby ->
            graph.putIfAbsent(nearby, Vector())
            graph[nearby]!! += chunk
            graph[chunk]!! += nearby
        }
    }
    //图生成好啦
    //接下来搜索最大环，也就刷接通所有chunks的环
    //需要用到单向DFS深度优先搜索
    //现在我们随便选取一个起点
    val start = chunks[0]
    val visited = chunks.associateWith { false }.toMutableMap()
    val edges = mutableListOf<Pair<Chunk, Chunk>>()
    fun dfs(current: Chunk): Boolean {
        //如果连满了，回到起点了
        if (current == start && !visited.values.contains(false)) {
            return true
        }
        //本条搜搜已经访问过了
        if (visited[current]!!)
            return false
        //加标记
        visited[current] = true
        //搜点
        graph[current]!!.forEach { nearby ->
            val pair = current to nearby
            //尝试加边
            edges += pair
            if (dfs(nearby)) {
                //成了，返回
                return true
            } else {
                //本条搜索路线没成，标记还原，把边删掉
                visited[nearby] = false
                edges -= pair
            }
        }
        //全部搜完了还没有true，只能返回false了
        return false
    }

    val dfsResult = dfs(start)
  //  if (!dfsResult) {
    //    //TODO 你的区块没有连接成环，而是一条线呜呜呜
    //}
//
//    graph.clear()
//    edges.forEach { (u, v) ->
//        graph.putIfAbsent(u, Vector())
//        graph.putIfAbsent(v, Vector())
//        graph[u]!! += v
//        graph[v]!! += u
//    }
//
//    val result = mutableListOf<Pair<Location, Location>>()
//
//    chunks.forEach { visited[it] = false }
//    fun connect(last: Chunk, current: Chunk) {
//        if (!visited.values.contains(false)) {
//            //连接头尾
//        }
//        if (visited[current]!!)
//            return
//        visited[current] = true
//        val next = graph[current]!![0]
//
//
//
//        connect(current, next)
//    }

    return edges
}