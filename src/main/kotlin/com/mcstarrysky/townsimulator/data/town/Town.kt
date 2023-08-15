package com.mcstarrysky.townsimulator.data.town

import com.mcstarrysky.townsimulator.data.common.container.AdminContainer
import com.mcstarrysky.townsimulator.data.common.container.HeadContainer
import com.mcstarrysky.townsimulator.data.common.container.MemberContainer
import com.mcstarrysky.townsimulator.data.common.container.RegionContainer
import com.mcstarrysky.townsimulator.data.data
import com.mcstarrysky.townsimulator.data.entity.Admin
import com.mcstarrysky.townsimulator.data.entity.Member
import com.mcstarrysky.townsimulator.data.entity.Owner
import com.mcstarrysky.townsimulator.data.chunk.Chunk
import com.mcstarrysky.townsimulator.data.Storage
import com.mcstarrysky.townsimulator.data.common.*
import com.mcstarrysky.townsimulator.data.entity.Human
import com.mcstarrysky.townsimulator.info
import com.mcstarrysky.townsimulator.data.request.Request
import com.mcstarrysky.townsimulator.data.serializer.UUIDListConverter
import com.mcstarrysky.townsimulator.data.vote.Vote
import com.mcstarrysky.townsimulator.data.vote.VoteType
import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import taboolib.module.lang.asLangText
import taboolib.platform.util.asLangText
import java.util.*

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.town.Town
 *
 * @author Mical
 * @since 2023/7/24 14:29
 */
class Town(
    @Conversion(UUIDConverter::class)
    private var owner: UUID,
    @Conversion(UUIDListConverter::class)
    private val admins: MutableList<UUID>,
    @Conversion(UUIDListConverter::class)
    private val members: MutableList<UUID>,
    private val chunks: MutableList<Chunk>,
    override val name: String,
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID,
    override val timestamp: Long,
    val enterMessage: String = "${console().asLangText("prefix")} &{#C8A384}你进入了聚落 $name 的区域。",
    val leaveMessage: String = "${console().asLangText("prefix")} &{#C8A384}你离开了聚落 $name 的区域。"

) : Unique, Nameable, Votable, VoteInitiator, Timestamp, AdminContainer, HeadContainer, MemberContainer, RegionContainer {

    @Transient
    var showingFrame = false

    fun getRequests(): List<Request> {
        return Storage.requests.values.filter { it.townUUID == uniqueId }
    }

    override fun vote(vote: Vote, result: VoteType) {
        when (result) {
            VoteType.SUPPORT -> vote.supporters += uniqueId
            VoteType.OPPONENT -> vote.opponents += uniqueId
            VoteType.ABSTAIN -> vote.abstainers += uniqueId
        }
    }

    override fun isVotable(): Boolean {
        return true
    }

    override fun startVote(vote: Vote) {
        vote.start()
    }

    override fun stopVote(vote: Vote) {
        vote.stopByInitiator = true
        vote.stop()
    }

    override fun getOwner(): Owner {
        return data<Owner>(owner) ?: error("Town $name($uniqueId) doesn't have a owner!")
    }

    override fun changeOwner(owner: Owner) {
        val origin = Bukkit.getOfflinePlayer(this.owner)
        val member = Member(origin.uniqueId, uniqueId, getOwner().timestamp)
        val now = Bukkit.getOfflinePlayer(owner.uniqueId)
        // 发送成功信息
        origin.info("已成功将聚落负责人职位转让给 ${now.name}, 你已退位至聚落普通成员")
        // 更换聚落负责人
        this.owner = owner.uniqueId
        // 切换原负责人身份
        Storage.players += origin.uniqueId to member
        // 切换现负责人
        Storage.players += owner.uniqueId to owner
        // 发送通知至聚落全体成员
        for (p in members.map { Bukkit.getOfflinePlayer(it) }) {
            p.info("经决定, 聚落现负责人已从 ${origin.name} 更换至 ${now.name}, 原负责人已自动退位至普通成员")
        }
    }

    override fun getAdmins(): List<Admin> {
        return admins.map { data<Admin>(it) ?: error("Unknown administrator $it in town $name($uniqueId)!") }
    }

    override fun addAdmin(admin: Admin) {
        admins += admin.uniqueId
        Storage.players[admin.uniqueId] = admin
    }

    override fun delAdmin(admin: Admin) {
        admins -= admin.uniqueId
        Storage.players[admin.uniqueId] = Member(admin.uniqueId, uniqueId, admin.timestamp)
    }

    override fun getMembers(): List<Member> {
        return members.map { data<Member>(it) ?: error("Unknown member $it in town $name($uniqueId)!") }
    }

    override fun addMember(member: Member) {
        members += member.uniqueId
        Storage.players[member.uniqueId] = member
    }

    override fun delMember(member: Member) {
        members -= member.uniqueId
        Storage.players[member.uniqueId] = Human(member.uniqueId)
    }

    override fun getRegions(): List<Chunk> {
        return chunks
    }

    override fun addRegion(chunk: Chunk) {
        chunks += chunk
    }

    override fun delRegion(chunk: Chunk) {
        chunks -= chunk
    }
}