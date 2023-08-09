package com.github.zhibei.core.objective.dragoncore

import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("DragonCore")
object DragonPlayerSlotUpdate : ObjectiveCountableI<PlayerSlotUpdateEvent>() {

    override val name = "dragoncore player slot update"
    override val event = PlayerSlotUpdateEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("identifier") { data, it ->
            it.identifier.contains(data.toString())
        }
        addSimpleCondition("name") { data, it ->
            it.itemStack.itemMeta?.displayName == data.toString()
        }
        addSimpleCondition("lore") { data, it ->
            it.itemStack.itemMeta?.lore?.contains(data.toString()) ?: false
        }
    }
}