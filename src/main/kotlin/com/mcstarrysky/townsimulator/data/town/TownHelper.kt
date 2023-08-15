package com.mcstarrysky.townsimulator.data.town

import com.mcstarrysky.townsimulator.TownConfig
import com.mcstarrysky.townsimulator.data.chunk.Chunk
import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Admin
import com.mcstarrysky.townsimulator.data.entity.Human
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.data.entity.Owner
import com.mcstarrysky.townsimulator.generateEdges
import com.mcstarrysky.townsimulator.getCircle
import com.mcstarrysky.townsimulator.info
import com.mcstarrysky.townsimulator.ingame.ui.TownControlUI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import taboolib.common5.cdouble
import taboolib.module.effect.createLine
import taboolib.platform.util.nextChat
import taboolib.platform.util.toProxyLocation
import java.util.*

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.town.TownHelper
 *
 * @author Mical
 * @since 2023/7/24 16:40
 */
object TownHelper {

    /**
     * 创建一个聚落
     */
    fun createTown(player: Player, name: String) {
        val human = data<Human>(player.uniqueId)!!
        if (human is Member) {
            player.info("你已存在于一聚落中, 不可以再创建新的聚落")
            return
        }
        if (player.world.name != TownConfig.world) {
            player.info("该制度是为玉琴星设计的, 请在玉琴星上创建")
            return
        }
        val time = System.currentTimeMillis()
        val owner = Owner(human.uniqueId, timestamp = time)
        player.info("请指定一名玩家成为聚落协助者, 该玩家必须在线")
        player.info("这个玩家必须为你申请创建聚落邮件中提交的玩家")
        player.nextChat { adminName ->
            val p = getProxyPlayer(adminName)
            if (p == null) {
                player.info("你指定的协助者不在线, 无法继续")
                return@nextChat
            }
            val adminHuman = data<Human>(p.uniqueId)!!
            if (adminHuman is Member) {
                player.info("你指定的协助者已经是其他聚落的成员了!")
                player.info("如果你指定的协助者无法前来, 你可能需要重新提交申请")
                return@nextChat
            }
            val admin = Admin(p.uniqueId, timestamp = time)
            val chunk = player.location.chunk
            // 计算间隔数量 (FIXME: 暂定在50个区块外)
            val regions = Storage.towns.values.map { it.getRegions() }
            if (regions.all { r -> r.all { it.calculate(chunk) > 50 } }) {
                val townUUID = UUID.randomUUID()
                val town = Town(
                    owner.uniqueId,
                    mutableListOf(admin.uniqueId),
                    mutableListOf(owner.uniqueId, admin.uniqueId),
                    mutableListOf(),
                    name,
                    townUUID,
                    time
                )
                for (region in chunk.getCircle(5).map { Chunk(it) }) { // 五圈
                    town.addRegion(region)
                }
                Storage.towns += townUUID to town
                owner.setTown(town)
                Storage.players += owner.uniqueId to owner
                admin.setTown(town)
                Storage.players += admin.uniqueId to admin
                player.info("聚落已建立成功!")
                TownControlUI.open(player)
            } else {
                player.info("检测到有的聚落离你的聚落较近! 为避免不必要的冲突请另择良地")
            }
        }
    }

    /**
     * 显示聚落区域边界
     */
    fun showFrame(town: Town, player: Player) {
        // 防止重复刷卡死
        if (town.showingFrame) return
        val pair = generateEdges(
            town.getRegions()
                .map {
                    (Bukkit.getWorld(it.world) ?: return).getChunkAt(it.x, it.z)
                }, (player.location.blockY + 2).cdouble
            // 拿到中心点
        ).map { it.first.getBlock(8, 0, 8).location to it.second.getBlock(8, 0, 8).location }
        val locs = mutableListOf<Location>()
        for ((f, s) in pair) {
            locs += f.toProxyLocation()
            locs += s.toProxyLocation()
        }
        (0 until locs.size).forEach { i ->
            createLine(locs[i], locs[i + 1], 5.0, 20 * 30L) // 半分钟后消失
        }
        town.showingFrame = true
        player.info("已成功为你展示聚落管辖区域的外围区块(区块中心点连线,非最外围)! 半分钟后消失")
        submit(delay = 20 * 30L) {
            town.showingFrame = false
        }
    }
}