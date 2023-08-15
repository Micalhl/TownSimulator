package com.mcstarrysky.townsimulator.data.common.container

import com.mcstarrysky.townsimulator.data.entity.Member

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.container.MemberContainer
 *
 * @author Mical
 * @since 2023/7/24 14:45
 */
interface MemberContainer {

    /**
     * 应包含聚落负责人和协助者
     */
    fun getMembers(): List<Member>

    /**
     * 添加成员
     */
    fun addMember(member: Member)

    /**
     * 删除成员
     */
    fun delMember(member: Member)
}