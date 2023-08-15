package com.mcstarrysky.townsimulator.data.common

import com.mcstarrysky.townsimulator.data.vote.Vote
import com.mcstarrysky.townsimulator.data.vote.VoteType

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.Votable
 *
 * @author Mical
 * @since 2023/7/24 10:39
 */
interface Votable {

    fun vote(vote: Vote, result: VoteType)

    fun isVotable(): Boolean
}