package com.mcstarrysky.townsimulator.data.request

import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.info
import com.mcstarrysky.townsimulator.data.town.Town
import org.bukkit.Bukkit
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.*

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.request.QuitRequest
 *
 * @author Mical
 * @since 2023/7/25 11:40
 */
class QuitRequest(
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
        val member = data<Member>(uniqueId)
        town.delMember(member!!)
        Bukkit.getOfflinePlayer(uniqueId).info("聚落 ${town.name} 已通过了你的退出申请.")
    }

    override fun reject(reason: String) {
        if (town == null) return
        Bukkit.getOfflinePlayer(uniqueId).info("聚落 ${town.name} 拒绝了你的退出申请! 理由: $reason")
    }
}