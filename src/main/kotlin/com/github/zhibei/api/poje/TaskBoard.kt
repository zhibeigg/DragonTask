package com.github.zhibei.api.poje

import eos.moe.dragontracker.data.WaypointData
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class TaskBoard(val viewer: Player) {

    val info = mutableMapOf<String, String>()

    val quests = mutableListOf<String>()

    val wayPoints = ConcurrentHashMap<String, WaypointData>()

}