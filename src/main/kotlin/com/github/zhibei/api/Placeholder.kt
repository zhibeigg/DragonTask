package com.github.zhibei.api

import com.github.zhibei.core.storage.Storage
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object Placeholder : PlaceholderExpansion {

    override val identifier: String = "dragontask"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return when (args) {
            "width" -> player?.let { Storage.INSTANCE.getUIWidth(it).toString() } ?: "1.5"
            else -> "null"
        }
    }
}