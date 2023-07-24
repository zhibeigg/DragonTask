package com.github.zhibei.util

import com.github.zhibei.DragonTask
import me.clip.placeholderapi.PlaceholderAPI
import me.goudan.gddtitle.api.GDDTitleAPI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.getDataFolder
import taboolib.common.util.replaceWithOrder
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.platform.util.actionBar
import java.io.File


fun Player.sendGDDTitle(message: String) {
    val text = PlaceholderAPI.setPlaceholders(this, message)
    GDDTitleAPI.sendAction(this, text.colored(), 500, 1000, 500)
}

fun ProxyCommandSender.sendHud(node: String, type: String, vararg args: Any) {
    val file = File(getDataFolder(), "lang/zh_CN.yml")
    val player = Bukkit.getPlayerExact(this.name) ?: return
    when(type) {
        "dragon" -> player.sendGDDTitle(Configuration.loadFromFile(file).getString(node, node)!!.replaceWithOrder(*args))
    }
}

fun ProxyCommandSender.sendActionBar(node: String, vararg args: Any) {
    val file = File(getDataFolder(), "lang/zh_CN.yml")
    Bukkit.getPlayerExact(this.name)
        ?.actionBar(Configuration.loadFromFile(file).getString(node, node)!!.replaceWithOrder(*args))
}

fun CommandSender.sendSpecialLang(node: String, vararg args: Any) {
    if (!DragonTask.mention) return
    when (DragonTask.mentionType) {
        "dragon" -> adaptCommandSender(this).sendHud(node, "dragon", *args)
        "normal" -> adaptCommandSender(this).sendActionBar(node, *args)
    }
}
