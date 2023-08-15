package com.mcstarrysky.townsimulator.data.mail

import com.mcstarrysky.townsimulator.data.common.Unique
import org.bukkit.Bukkit
import taboolib.common.platform.function.adaptPlayer
import taboolib.library.configuration.Conversion
import taboolib.module.chat.Components
import taboolib.module.configuration.UUIDConverter
import java.util.*

/**
 * 用于处理发送消息时接收方不在线的情况
 * 无实际「邮件」意义，只是好听罢了
 *
 * TownSimulator
 * com.mcstarrysky.townsimulator.data.mail.Mail
 *
 * @author Mical
 * @since 2023/7/25 11:16
 */
class Mail(
    @Conversion(UUIDConverter::class)
    override val uniqueId: UUID = UUID.randomUUID(),
    val player: UUID,
    private val message: String,
    private var send: Boolean = false
) : Unique {

    fun trySend(): Boolean {
        if (send) return false
        val player = Bukkit.getPlayer(this.player) ?: return false
        Components.parseRaw(message).sendTo(adaptPlayer(player))
        send = true
        return true
    }
}