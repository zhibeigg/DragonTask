package com.github.zhibei.api

import ink.ptms.chemdah.api.ChemdahAPI.chemdahProfile
import ink.ptms.chemdah.core.quest.addon.AddonDepend.Companion.isQuestDependCompleted
import ink.ptms.chemdah.core.quest.addon.AddonTrack.Companion.track
import ink.ptms.chemdah.core.quest.addon.AddonTrack.Companion.trackQuest
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object Placeholder : PlaceholderExpansion {

    override val identifier: String = "dragontask"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return if (player != null) {
            when(args) {
                "hud" -> getHud(player)
                else -> "未知"
            }
        } else {
            "未知"
        }
    }

    fun getHud(player: Player): String {
        val quest = player.chemdahProfile.trackQuest ?: return "null"
        val description = mutableListOf<String>()
        quest.taskMap.forEach {
            val task = it.value
            if (task.isQuestDependCompleted(player) && !task.isCompleted(player.chemdahProfile)) {
                val var1 = task.track()?.description ?: return@forEach
                val var2 = task.track()?.name ?: ""
                description += var2
                description += var1
            }
        }
        return PlaceholderAPI.setPlaceholders(player, description.joinToString("\n"))
    }

}
