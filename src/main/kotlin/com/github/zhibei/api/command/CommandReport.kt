package com.github.zhibei.api.command

import com.github.zhibei.api.ReportManager
import com.github.zhibei.core.pojo.Report
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import java.text.SimpleDateFormat
import java.util.*

@CommandHeader(name = "report")
object CommandReport {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val clear = subCommand {
        player {
            dynamic("days") {
                execute<ProxyCommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute
                    val days = argument.toInt()
                    val beforeTime = System.currentTimeMillis() - days*24*3600*100
                    ReportManager.clearReport(beforeTime, player)
                    sender.sendLang("clear-reports", player.name, ReportManager.getTimesOfReports(player))
                }
            }
        }
    }

    @CommandBody
    val add = subCommand {
        player {
            dynamic("flyer") {
                suggestion<ProxyCommandSender> { _, _ -> listOf("false", "true") }
                dynamic("killer") {
                    suggestion<ProxyCommandSender> { _, _ -> listOf("false", "true") }
                    dynamic("blocker") {
                        suggestion<ProxyCommandSender> { _, _ -> listOf("false", "true") }
                        dynamic("fucker") {
                            suggestion<ProxyCommandSender> { _, _ -> listOf("false", "true") }
                            dynamic("info") {
                                execute<ProxyCommandSender> { sender, context, argument ->
                                    val cheater = Bukkit.getPlayerExact(context["player"]) ?: return@execute
                                    val option = listOf(context["flyer"].toBoolean(), context["killer"].toBoolean(), context["blocker"].toBoolean(), context["fucker"].toBoolean())
                                    val report = Report(UUID.randomUUID(),null, cheater, argument, option, System.currentTimeMillis())
                                    ReportManager.saveReport(report)
                                    sender.sendLang("add-report")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val remove = subCommand {
        dynamic("UUID") {
            execute<ProxyCommandSender> { sender, context, argument ->
                val uuid = UUID.fromString(argument) ?: return@execute
                ReportManager.removeReport(uuid)
                sender.sendLang("remove-report")
            }
        }
    }

    @CommandBody
    val list = subCommand {
        player {
            execute<ProxyCommandSender> { sender, _, argument ->
                val player = Bukkit.getPlayerExact(argument) ?: return@execute
                ReportManager.getReports(player).forEach {
                    sender.sendLang(
                        "report-list",
                        convertLongToTime(it.time),
                        it.flyer.simple(),
                        it.killer.simple(),
                        it.blocker.simple(),
                        it.fucker.simple(),
                        it.reporter?.name ?: "无",
                        it.reportInfo
                    )
                }
            }
        }
    }

    fun Boolean.simple(): String{
        return if (this) {
            "&a是".colored()
        } else {
            "&4否".colored()
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }


}