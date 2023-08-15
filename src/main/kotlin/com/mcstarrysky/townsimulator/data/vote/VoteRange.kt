package com.mcstarrysky.townsimulator.data.vote

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.vote.VoteType
 *
 * @author mical
 * @since 2023/8/10 1:21 PM
 */
enum class VoteRange {

    TOWN, // 以聚落为单位进行投票
    IN_TOWN, // 聚落内部投票
    SERVER // 全服投票
}