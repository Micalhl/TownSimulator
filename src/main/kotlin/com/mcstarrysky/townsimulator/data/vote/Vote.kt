package com.mcstarrysky.townsimulator.data.vote

import com.mcstarrysky.townsimulator.data.common.Nameable
import com.mcstarrysky.townsimulator.data.common.Timestamp
import com.mcstarrysky.townsimulator.data.common.Unique
import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.info
import com.mcstarrysky.townsimulator.data.town.Town
import org.bukkit.Bukkit
import taboolib.common.util.unsafeLazy
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.*

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.vote.Vote
 *
 * @author Mical
 * @since 2023/7/29 00:22
 */
class Vote(
    override val name: String,
    override val timestamp: Long,
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID,
    val initiator: UUID,
    val voteRange: VoteRange, // 投票类型
    val resultType: ResultType, // 结果类型, 多数服从少数或少数服从多数
    var stopByInitiator: Boolean = false, // 是否自行关闭
    @Conversion(UUIDConverter::class)
    val townUUID: UUID = UUID(0, 0),
    val supporters: MutableList<UUID> = mutableListOf(), // 支持者
    val opponents: MutableList<UUID> = mutableListOf(), // 反对者
    val abstainers: MutableList<UUID> = mutableListOf(), // 弃权者
    val isAnonymous: Boolean = false, // 是否为匿名投票
    var isDeprecated: Boolean = false,
) : Unique, Nameable, Timestamp {

    @delegate:Transient
    private val town: Town by unsafeLazy {
        data<Town>(townUUID) ?: error("unknown town: $townUUID")
    }

    fun start() {
        Storage.votes += uniqueId to this
        when (voteRange) {
            VoteRange.TOWN -> Storage.towns.map { it.value.getOwner().bkOfflinePlayer }.forEach {
                it.info("你有新的「聚落」投票, 请打开投票面板查看")
            }
            VoteRange.IN_TOWN -> town.getMembers().map { it.bkOfflinePlayer }.forEach {
                it.info("你有新的「聚落内部」投票, 请打开投票面板查看")
            }
            VoteRange.SERVER -> Bukkit.getOfflinePlayers().map {
                it.info("你有新的「全服」投票, 请打开投票面板查看")
            }
        }
    }

    fun stop() {
        isDeprecated = true
        val result = if (calculateResult()) "通过" else "不通过"
        when (voteRange) {
            VoteRange.TOWN -> Storage.towns.map { it.value.getOwner().bkOfflinePlayer }.forEach {
                it.info("投票 $name 已结束! 结果为: $result")
            }
            VoteRange.IN_TOWN -> town.getMembers().map { it.bkOfflinePlayer }.forEach {
                it.info("投票 $name 已结束! 结果为: $result")
            }
            VoteRange.SERVER -> Bukkit.getOfflinePlayers().map {
                it.info("投票 $name 已结束! 结果为: $result")
            }
        }
    }

    private fun calculateResult(): Boolean {
        return if (resultType == ResultType.MORE && supporters.size > opponents.size) true
        else if (resultType == ResultType.LESS && opponents.size < supporters.size) false
        else false
    }
}