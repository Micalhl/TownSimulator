package com.mcstarrysky.townsimulator.data.entity

import com.mcstarrysky.townsimulator.data.common.Unique
import com.mcstarrysky.townsimulator.data.common.Votable
import com.mcstarrysky.townsimulator.data.vote.Vote
import com.mcstarrysky.townsimulator.data.vote.VoteType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.library.configuration.Conversion
import taboolib.module.configuration.UUIDConverter
import java.util.UUID

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.entity.Member
 *
 * @author Mical
 * @since 2023/7/24 00:44
 */
open class Human(
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID
) : Unique, Votable {

    @delegate:Transient
    val bkPlayer: Player? by unsafeLazy {
        Bukkit.getPlayer(uniqueId)
    }

    @delegate:Transient
    val bkOfflinePlayer: OfflinePlayer by unsafeLazy {
        Bukkit.getOfflinePlayer(uniqueId)
    }

    val location: Location?
        get() = bkPlayer?.location

    override fun isVotable(): Boolean {
        return true
    }

    override fun vote(vote: Vote, result: VoteType) {
        when (result) {
            VoteType.SUPPORT -> vote.supporters += uniqueId
            VoteType.OPPONENT -> vote.opponents += uniqueId
            VoteType.ABSTAIN -> vote.abstainers += uniqueId
        }
    }

    override fun toString(): String {
        return "Human(uniqueId=$uniqueId)"
    }
}
