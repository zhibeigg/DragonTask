package com.github.zhibei.api

import com.github.zhibei.storage.Storage
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import taboolib.common.platform.event.SubscribeEvent

object GUIManager {

    @SubscribeEvent
    fun packet(e: CustomPacketEvent) {
        if (e.identifier == "dragonui") {
            val player = e.player
            val width = e.data[0].toDouble()
            Storage.INSTANCE.setUIWidth(player, width)
        }
    }

}