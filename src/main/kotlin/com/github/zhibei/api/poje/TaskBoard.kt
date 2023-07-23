package com.github.zhibei.api.poje

import org.bukkit.entity.Player

class TaskBoard(val viewer: Player) {

    val info = mutableMapOf<String, String>()

    val quests = mutableListOf<String>()

}