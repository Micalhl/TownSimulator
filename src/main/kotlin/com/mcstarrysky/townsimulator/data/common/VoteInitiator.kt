package com.mcstarrysky.townsimulator.data.common

import com.mcstarrysky.townsimulator.data.vote.Vote

/**
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.common.VoteInitiator
 *
 * @author Mical
 * @since 2023/7/29 00:20
 */
interface VoteInitiator {

    fun startVote(vote: Vote) {
        vote.start()
    }

    fun stopVote(vote: Vote) {
        vote.stopByInitiator = true
        vote.stop()
    }
}