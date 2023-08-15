package com.mcstarrysky.townsimulator.data.common.container

import com.mcstarrysky.townsimulator.data.entity.Admin

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.container.AdminContainer
 *
 * @author Mical
 * @since 2023/7/24 14:46
 */
interface AdminContainer {

    /**
     * 获取聚落协助者成员列表
     */
    fun getAdmins(): List<Admin>

    /**
     * 添加协助者
     */
    fun addAdmin(admin: Admin)

    /**
     * 删除协助者
     */
    fun delAdmin(admin: Admin)
}