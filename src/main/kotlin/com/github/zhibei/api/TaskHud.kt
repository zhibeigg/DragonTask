package com.github.zhibei.api

import com.github.zhibei.api.poje.TaskBoard
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import eos.moe.dragontracker.DragonTracker
import eos.moe.dragontracker.data.WaypointData
import ink.ptms.chemdah.api.ChemdahAPI.chemdahProfile
import ink.ptms.chemdah.core.quest.addon.AddonDepend.Companion.isQuestDependCompleted
import ink.ptms.chemdah.core.quest.addon.AddonTrack.Companion.track
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.SubscribeEvent

object TaskHud {

    val boards = mutableMapOf<String, TaskBoard>()

    @Schedule(async = true, period = 5)
    fun refreshQuest() {
        Bukkit.getOnlinePlayers().forEach {
            getHud(it).sendTask()
        }
    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        val taskBoard = TaskBoard(e.player)
        boards[e.player.name] = taskBoard
        getHud(e.player).sendTask()
    }

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        boards.remove(e.player.name)
    }

    @SubscribeEvent
    fun packet(e: CustomPacketEvent) {
        if (e.identifier == "dragontask") {
            val player = e.player
            val id = e.data[0]
            val quest = player.chemdahProfile.getQuests().find { it.id == id }!!
            quest.tasks.forEach { task ->
                if (task.isQuestDependCompleted(player) && !task.isCompleted(player.chemdahProfile)) {
                    val location = task.track()?.center?.getLocation(player) ?: return
                    val name = task.track()?.name ?: id
                    DragonTracker.getInstance().packetHandler.sendAddWaypoint(player, WaypointData(name, listOf("&e目的地"), "", location.world?.name ?: player.world.name, location.x, location.y, location.z, 3, 300))
                }
            }
        }
    }

    private fun getHud(player: Player): TaskBoard {
        val quests = player.chemdahProfile.getQuests()
        val taskBoard = boards[player.name]!!
        taskBoard.quests.clear()
        taskBoard.info.clear()
        quests.forEach {  quest ->
            val description = mutableListOf<String>()
            if (quest.isValid && !quest.isCompleted) {
                taskBoard.quests += quest.id
                quest.tasks.forEach task@{ task ->
                    if (task.isQuestDependCompleted(player) && !task.isCompleted(player.chemdahProfile)) {
                        val var1 = task.track()?.description ?: return@task
                        val var2 = task.track()?.name ?: ""
                        description += var2
                        description += var1
                    }
                }
            }
            taskBoard.info[quest.id] = PlaceholderAPI.setPlaceholders(player, description.joinToString("\n"))
        }
        boards[player.name] = taskBoard
        return taskBoard
    }

    private fun TaskBoard.sendTask() {
        PacketSender.sendSyncPlaceholder(viewer, info)
        PacketSender.sendSyncPlaceholder(viewer, mapOf("dragontask_quests" to quests.joinToString(":")))
    }

}
