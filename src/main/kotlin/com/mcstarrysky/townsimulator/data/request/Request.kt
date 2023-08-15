package com.mcstarrysky.townsimulator.data.request

import com.mcstarrysky.townsimulator.data.common.Nameable
import com.mcstarrysky.townsimulator.data.common.Timestamp
import com.mcstarrysky.townsimulator.data.common.Unique
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.*

/**
 * 这里实现的 Nameable 就是玩家加入聚落的理由
 *
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.request.Request
 *
 * @author Mical
 * @since 2023/7/25 11:42
 */
open class Request(
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID,
    override val name: String,
    override val timestamp: Long,
    @Conversion(UUIDConverter::class)
    open val townUUID: UUID
) : Unique, Nameable, Timestamp {

    open fun accept() {
    }

    open fun reject(reason: String) {
    }
}