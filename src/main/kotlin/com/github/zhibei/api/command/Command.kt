package com.github.zhibei.api.command

import com.github.zhibei.DragonTask
import com.github.zhibei.DragonTask.config
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

@CommandHeader(name = "dragontask", aliases = ["task"], permission = "dragontask.commmand")
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reload = subCommand {

        execute<ProxyCommandSender> { sender, _, _ ->
            config.reload()
            sender.sendMessage("reload successful.")
        }

    }

    @CommandBody
    val reg = subCommand {

        execute<ProxyCommandSender> { _, _, _ ->
            DragonTask.reg()
        }

    }

    @CommandBody
    val mm = CommandMM

}