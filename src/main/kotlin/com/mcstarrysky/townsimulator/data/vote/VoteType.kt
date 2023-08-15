package com.mcstarrysky.townsimulator.data.vote

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.vote.VoteType
 *
 * @author mical
 * @since 2023/8/10 1:59 PM
 */
enum class VoteType(val typeName: String) {

    SUPPORT("支持"), // 支持
    OPPONENT("反对"), // 反对
    ABSTAIN("弃权") // 弃权
}