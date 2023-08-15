package com.mcstarrysky.townsimulator.data.entity

import com.mcstarrysky.townsimulator.TownConfig
import com.mcstarrysky.townsimulator.data.common.Owner
import com.mcstarrysky.townsimulator.data.common.VoteInitiator
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.entity.Owner
 *
 * @author Mical
 * @since 2023/7/24 14:17
 */
class Owner(
    uuid: UUID,
    town: UUID = UUID.randomUUID(),
    timestamp: Long
) : Member(uuid, town, timestamp), Owner, VoteInitiator {

    override fun getRoleName(): String {
        return TownConfig.config.getString("role.owner.name", "负责人")!!
    }

    override fun toString(): String {
        return "Owner(uniqueId=$uniqueId)"
    }

    override fun sendNotice(content: String) {
        TODO("Not yet implemented")
    }
}