package com.github.zhibei.api

import com.github.zhibei.DragonTask.clearDistance
import com.github.zhibei.api.DragonTaskAPI.getTaskBoard
import com.github.zhibei.api.poje.TaskBoard
import com.github.zhibei.util.files
import com.github.zhibei.util.sendSpecialLang
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import eos.moe.dragontracker.DragonTracker
import eos.moe.dragontracker.data.WaypointData
import ink.ptms.chemdah.api.ChemdahAPI.chemdahProfile
import ink.ptms.chemdah.api.ChemdahAPI.isChemdahProfileLoaded
import ink.ptms.chemdah.api.event.collect.PlayerEvents
import ink.ptms.chemdah.core.quest.addon.AddonDepend.Companion.isQuestDependCompleted
import ink.ptms.chemdah.core.quest.addon.AddonTrack.Companion.track
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.mapSection
import java.util.concurrent.ConcurrentHashMap

object TaskHud {

    val boards = ConcurrentHashMap<String, TaskBoard>()

    private val waypointDatas = ConcurrentHashMap<String, WaypointData>()

    @Awake(LifeCycle.ENABLE)
    fun reload() {
        waypointDatas.clear()
        files("waypoint", listOf("example.yml")) {
            Configuration.loadFromFile(it).mapSection { config ->
                waypointDatas[config.name] = WaypointData(
                    config.name,
                    config.getStringList("label"),
                    config.getString("resource", "zb.gif"),
                    Bukkit.getWorlds()[0].name,
                    0.0,
                    0.0,
                    0.0,
                    0,
                    config.getInt("distance", 0)
                )
            }
        }
    }


    @Schedule(period = 5, async = true)
    fun trackerUpdate() {
        Bukkit.getOnlinePlayers().filter { it.isChemdahProfileLoaded }.forEach { player ->
            refreshQuest(player)
            clearWayPoint(player)
        }
    }

    fun refreshQuest(player: Player) {
        getHud(player).sendTask()
    }

    @SubscribeEvent
    fun e(e: PlayerEvents.Selected) {
        val taskBoard = TaskBoard(e.player)
        boards[e.player.name] = taskBoard
        refreshQuest(e.player)
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
                    val name = task.track()?.name ?: task.id
                    val world = location.world?.name ?: player.world.name
                    val waypointData = (waypointDatas[task.id] ?: waypointDatas["default"] ?: error("无${task.id}的导航配置, 也无默认配置")).apply {
                        key = task.id
                        x = location.x
                        y = location.y
                        z = location.z
                        worldName = world
                        val new = mutableListOf<String>()
                        label.forEach {
                            new += it.replace("{task}", name).replace("{quest}", id)
                        }
                        label = new
                    }
                    if (player.world.name == world) {
                        player.sendSpecialLang("gps-start", name)
                        DragonTracker.getInstance().packetHandler.sendAddWaypoint(player, waypointData)
                        val board = player.getTaskBoard()
                        board.wayPoints[id] = waypointData
                        boards[player.name] = board
                    } else {
                        player.sendSpecialLang("world-message", world)
                    }
                }
            }
        }
    }

    private fun clearWayPoint(player: Player) {
        player.chemdahProfile.getQuests().forEach {  quest ->
            if (quest.isValid && !quest.isCompleted) {
                quest.tasks.forEach task@{ task ->
                    if (task.isQuestDependCompleted(player) && !task.isCompleted(player.chemdahProfile)) {
                        val point = player.getTaskBoard().wayPoints[quest.id] ?: return@task
                        val location = Location(Bukkit.getWorld(point.worldName), point.x, point.y, point.z)
                        if (player.location.distance(location) <= clearDistance) {
                            DragonTracker.getInstance().packetHandler.sendRemoveWaypoint(player, task.id)
                            val board = player.getTaskBoard()
                            board.wayPoints.remove(quest.id)
                            boards[player.name] = board
                        }
                    }
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

    fun pointTable(player: Player): Map<String, String> {
        val table = mutableListOf<String>()
        val taskBoard = player.getTaskBoard()
        taskBoard.quests.forEach {
            if (taskBoard.wayPoints.contains(it)) {
                table.add("是")
            } else {
                table.add("否")
            }
        }
        return mapOf("dragontask_waypoints" to table.joinToString(":"))
    }

    private fun TaskBoard.sendTask() {
        PacketSender.sendSyncPlaceholder(viewer, info)
        PacketSender.sendSyncPlaceholder(viewer, mapOf("dragontask_quests" to quests.joinToString(":")))
        PacketSender.sendSyncPlaceholder(viewer, pointTable(viewer))
    }

}
