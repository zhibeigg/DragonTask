package com.github.zhibei.api

import com.bh.planners.api.ManaCounter.toCurrentMana
import com.bh.planners.api.ManaCounter.toMaxMana
import com.bh.planners.api.PlannersAPI.plannersProfile
import com.github.zhibei.storage.Storage
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.serverct.ersha.dungeon.DungeonPlus
import org.serverct.ersha.dungeon.common.team.type.PlayerStateType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync

object GUIManager {

    @SubscribeEvent
    fun packet(e: CustomPacketEvent) {
        if (e.identifier == "dragonui") {
            val player = e.player
            val width = e.data[0].toDouble()
            Storage.INSTANCE.setUIWidth(player, width)
        }
    }

    @SubscribeEvent
    fun tab(e: CustomPacketEvent) {
        if (e.identifier == "dragontab") {
            val player = e.player
            submitAsync {
                sendTabPacket(player)
            }
        }
    }

    fun sendTabPacket(player: Player) {
        val onlines = Bukkit.getOnlinePlayers()
        val onlineNames = onlines.joinToString(":") { it.name }
        val onlineDisplayNames = onlines.joinToString(":") { it.displayName }
        val players = DungeonPlus.teamManager.getTeam(player)?.getPlayers(PlayerStateType.ALL) ?: return
        val names = players.joinToString(":") { it.displayName }
        val healths = players.joinToString(":") { it.health.toString() }
        val maxHealth = players.joinToString(":") { (it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0).toString() }
        val manas = players.joinToString(":") { it.plannersProfile.toCurrentMana().toString() }
        val maxManas = players.joinToString(":") { it.plannersProfile.toMaxMana().toString() }
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_names" to names))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_healths" to healths))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_maxhealth" to maxHealth))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_manas" to manas))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_maxmanas" to maxManas))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlinenames" to onlineNames))
        PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlinedisplaynames" to onlineDisplayNames))
    }

}