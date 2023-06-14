package com.github.zhibei.api.command

import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper


@CommandHeader(name = "taskmm")
object CommandMM {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val clear = subCommand {
        dynamic("type") {
            suggestion<ProxyCommandSender> { _, _ -> MythicMobs.inst().mobManager.mobTypes.map { it.internalName } }
            dynamic("world") {
                suggestion<ProxyCommandSender> { _, _ -> Bukkit.getWorlds().map { it.name } }
                execute<ProxyCommandSender> { _, context, argument ->
                    val type = MythicMobs.inst().mobManager.activeMobs.filter { it.mobType == context["type"] }
                    type.forEach {
                        if (it.entity.world.name.uppercase() == argument.uppercase()) {
                            it.entity.remove()
                        }
                    }
                }
            }
        }

    }

}