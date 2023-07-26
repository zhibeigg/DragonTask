package com.github.zhibei.api

import com.github.zhibei.api.poje.TaskBoard
import org.bukkit.entity.Player

object DragonTaskAPI {

    fun Player.getTaskBoard(): TaskBoard? {
        return TaskHud.boards[name]
    }

}