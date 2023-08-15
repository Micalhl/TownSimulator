package com.mcstarrysky.townsimulator.data.entity

import com.mcstarrysky.townsimulator.TownConfig
import com.mcstarrysky.townsimulator.data.common.Member
import com.mcstarrysky.townsimulator.data.common.Role
import com.mcstarrysky.townsimulator.data.common.Timestamp
import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.town.Town
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.entity.Member
 *
 * @author Mical
 * @since 2023/7/24 13:38
 */
open class Member(
    uuid: UUID,
    @Conversion(UUIDConverter::class)
    private var town: UUID = UUID.randomUUID(),
    override val timestamp: Long
) : Human(uuid), Member, Role, Timestamp {

    override fun isVotable(): Boolean {
        return System.currentTimeMillis() - bkOfflinePlayer.lastPlayed <= 86400000L * when (this) {
            is Admin -> 5L // 协助者五天后冻结其投票权
            else -> 7L // 负责人、聚落普通成员七天后冻结其投票权
        }
    }

    override fun getTown(): Town {
        return data<Town>(town) ?: error("Member $uniqueId is not in the town $town!")
    }

    override fun setTown(town: Town) {
        this.town = town.uniqueId
    }

    override fun getRoleName(): String {
        return TownConfig.config.getString("role.member.name", "成员")!!
    }

    override fun toString(): String {
        return "Member(uniqueId=$uniqueId)"
    }
}