package com.github.zhibei.api.command

import com.github.zhibei.core.storage.Storage
import eos.moe.dragoncore.api.SlotAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
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
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute
                    SlotAPI.setSlotItem(player, argument, ItemStack(Material.AIR), true)
                }
            }
        }
    }

    @CommandBody
    val set = subCommand {
        player {
            dynamic("width") {
                execute<ProxyCommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute
                    Storage.INSTANCE.setUIWidth(player, argument.toDouble())
                }
            }
        }
    }

}