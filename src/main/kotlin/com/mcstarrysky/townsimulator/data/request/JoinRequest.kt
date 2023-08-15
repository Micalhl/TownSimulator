package com.mcstarrysky.townsimulator.data.request

import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Human
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.info
import com.mcstarrysky.townsimulator.data.town.Town
import org.bukkit.Bukkit
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.request.JoinRequest
 *
 * @author Mical
 * @since 2023/7/25 00:17
 */
class JoinRequest(
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID,
    override val name: String,
    override val timestamp: Long,
    @Conversion(UUIDConverter::class)
    override val townUUID: UUID
) : Request(uniqueId, name, timestamp, townUUID) {

    @Transient
    val town = data<Town>(townUUID)

    override fun accept() {
        if (town == null) return
        val human = data<Human>(uniqueId)
        town.addMember(Member(human!!.uniqueId, town.uniqueId, System.currentTimeMillis()))
        Bukkit.getOfflinePlayer(uniqueId).info("聚落 ${town.name} 已通过了你的加入申请! [\\[&a请点击这里查看详情\\]](command=/town my;hover=点这里查看!)")
    }

    override fun reject(reason: String) {
        if (town == null) return
        Bukkit.getOfflinePlayer(uniqueId).info("聚落 ${town.name} 拒绝了你的加入申请! 理由: $reason")
    }
}