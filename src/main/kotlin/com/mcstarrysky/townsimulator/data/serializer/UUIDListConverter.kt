package com.mcstarrysky.townsimulator.data.serializer

import taboolib.common.Isolated
import taboolib.library.configuration.Converter
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.serializer.UUIDListConverter
 *
 * @author Mical
 * @since 2023/7/24 22:17
 */
@Isolated
class UUIDListConverter : Converter<List<UUID>, List<String>> {

    override fun convertToField(value: List<String>): List<UUID> {
        return value.map { UUID.fromString(it) }
    }

    override fun convertFromField(value: List<UUID>): List<String> {
        return value.map { it.toString() }
    }
}