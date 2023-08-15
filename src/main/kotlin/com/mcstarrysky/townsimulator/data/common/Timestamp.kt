package com.mcstarrysky.townsimulator.data.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.Timestamp
 *
 * @author Mical
 * @since 2023/7/28 17:19
 */
interface Timestamp {

    private val format: SimpleDateFormat
        get() = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")

    val timestamp: Long

    fun getTime(): String {
        return format.format(Date(timestamp))
    }
}