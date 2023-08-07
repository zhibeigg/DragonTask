package com.github.zhibei.api

import com.Zrips.CMI.CMI
import com.bh.planners.api.ManaCounter.toCurrentMana
import com.bh.planners.api.ManaCounter.toMaxMana
import com.bh.planners.api.PlannersAPI.plannersProfile
import com.github.zhibei.storage.Storage
import com.github.zhibei.util.sendSpecialLang
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.serverct.ersha.dungeon.DungeonPlus
import org.serverct.ersha.dungeon.common.team.type.PlayerStateType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submitAsync

object GUIManager {

    val cmi
        get() = CMI.getInstance()

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
            when (e.data[0]) {
                "打开" -> {
                    submitAsync {
                        sendTabPacket(player)
                    }
                }
                "单挑" -> {
                    player.performCommand("dantiao send ${e.data[1]}")
                }
                "组队" -> {
                    val target = Bukkit.getPlayerExact(e.data[1]) ?: return
                    val team = DungeonPlus.teamManager.getTeam(player) ?: run {
                        player.sendSpecialLang("not-team")
                        return
                    }
                    team.invite(player, target)
                }
                "私聊" -> {
                    if ((Bukkit.getPlayerExact(e.data[1]) ?: return).isOnline) {
                        player.performCommand("msg ${e.data[1]} ${e.data[2]}")
                    }else{
                        player.sendSpecialLang("offline", e.data[1])
                    }
                }
                "举报" -> {
                    TODO()
                }
            }
        }
    }

    fun sendTabPacket(player: Player) {

        val onlines = Bukkit.getOnlinePlayers().apply { info(this) }
        val cmis = onlines.map { cmi.playerManager.getUser(it) }

        onlines.joinToString(":") { it.name }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlinenames" to this))
        }

        onlines.joinToString(":") { it.displayName }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlinedisplaynames" to this))
        }

        cmis.joinToString(":") { it.isAfk.toString() }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlineafk" to this))
        }

        onlines.joinToString(":") { cmi.reflectionManager.getPing(it).toString() }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_onlinepings" to this))
        }

        val players = DungeonPlus.teamManager.getTeam(player)?.getPlayers(PlayerStateType.ALL) ?: return

        players.joinToString(":") { it.displayName }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_names" to this))
        }


        players.joinToString(":") { it.health.toString() }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_healths" to this))
        }

        players.joinToString(":") { (it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0).toString() }
            .apply {
                PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_maxhealth" to this))
            }


        players.joinToString(":") { it.plannersProfile.toCurrentMana().toString() }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_manas" to this))
        }

        players.joinToString(":") { it.plannersProfile.toMaxMana().toString() }.apply {
            PacketSender.sendSyncPlaceholder(player, mapOf("dragontab_maxmanas" to this))
        }


    }

}