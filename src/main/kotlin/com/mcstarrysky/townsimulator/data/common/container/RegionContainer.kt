package com.mcstarrysky.townsimulator.data.common.container

import com.mcstarrysky.townsimulator.data.chunk.Chunk

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.container.RegionContainer
 *
 * @author Mical
 * @since 2023/7/24 16:29
 */
interface RegionContainer {

    /**
     * 获取该聚落所持有的区块
     */
    fun getRegions(): List<Chunk>

    /**
     * 占领一个区块
     */
    fun addRegion(chunk: Chunk)

    /**
     * 放弃一个区块
     */
    fun delRegion(chunk: Chunk)


}