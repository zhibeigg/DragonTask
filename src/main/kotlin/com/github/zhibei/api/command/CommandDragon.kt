package com.github.zhibei.api.command

import eos.moe.dragoncore.api.SlotAPI
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper


@CommandHeader(name = "taskdragon")
object CommandDragon {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val clear = subCommand {
        dynamic("player") {
            suggestion<ProxyCommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic("slot") {
                execute<ProxyCommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayerExact(context["player"])
                    SlotAPI.setSlotItem(player, argument, null, true)
                }
            }
        }
    }

}