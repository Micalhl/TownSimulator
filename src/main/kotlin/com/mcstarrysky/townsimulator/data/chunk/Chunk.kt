package com.mcstarrysky.townsimulator.data.chunk

import com.mcstarrysky.townsimulator.data.entity.Human
import org.bukkit.Chunk
import org.bukkit.Location
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.chunk.Chunk
 *
 * @author Mical
 * @since 2023/7/24 16:30
 */
data class Chunk(
    val world: String,
    val x: Int,
    val z: Int
) {

    constructor(chunk: Chunk) : this(chunk.world.name, chunk.x, chunk.z)

    fun checkIn(chunk: Chunk): Boolean {
        return chunk.world.name == world && chunk.x == x && chunk.z == z
    }

    fun checkIn(location: Location): Boolean {
        return checkIn(location.chunk)
    }

    fun checkIn(human: Human): Boolean {
        return checkIn(human.location!!)
    }

    /**
     * 是否处于保留区域
     */
    fun inReservedArea(human: Human): Boolean {
        return checkIn(human) && human.location!!.y < 0
    }

    /**
     * 计算两个区块间隔区块数
     */
    fun calculate(chunk: Chunk): Int {
        val deltaX = abs(chunk.x - x).toDouble()
        val deltaZ = abs(chunk.z - z).toDouble()
        return sqrt(deltaX * deltaX + deltaZ * deltaZ).roundToInt() // 勾股定理后四舍五入
    }
}
