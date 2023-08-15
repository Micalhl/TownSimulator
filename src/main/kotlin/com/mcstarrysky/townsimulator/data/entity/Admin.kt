package com.mcstarrysky.townsimulator.data.entity

import com.mcstarrysky.townsimulator.TownConfig
import com.mcstarrysky.townsimulator.data.common.Administrator
import com.mcstarrysky.townsimulator.data.common.VoteInitiator
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.entity.Admin
 *
 * @author Mical
 * @since 2023/7/24 13:37
 */
class Admin(
    uuid: UUID,
    town: UUID = UUID.randomUUID(),
    timestamp: Long
) : Member(uuid, town, timestamp), Administrator, VoteInitiator {

    override fun getRoleName(): String {
        return TownConfig.config.getString("role.administrator.name", "协助者")!!
    }

    override fun toString(): String {
        return "Admin(uniqueId=$uniqueId)"
    }
}