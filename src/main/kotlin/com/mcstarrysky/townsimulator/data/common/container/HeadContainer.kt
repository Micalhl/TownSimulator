package com.mcstarrysky.townsimulator.data.common.container

import com.mcstarrysky.townsimulator.data.entity.Owner

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.container.HeadContainer
 *
 * @author Mical
 * @since 2023/7/24 14:47
 */
interface HeadContainer {

    /**
     * 获取聚落负责人
     */
    fun getOwner(): Owner

    /**
     * 转让聚落负责人
     */
    fun changeOwner(owner: Owner)
}