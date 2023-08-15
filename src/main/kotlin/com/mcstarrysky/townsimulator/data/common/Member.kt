package com.mcstarrysky.townsimulator.data.common

import com.mcstarrysky.townsimulator.data.town.Town

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.Member
 *
 * @author Mical
 * @since 2023/7/24 13:38
 */
interface Member {

    fun getTown(): Town

    fun setTown(town: Town)
}