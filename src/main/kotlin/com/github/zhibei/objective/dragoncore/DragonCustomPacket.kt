package com.github.zhibei.objective.dragoncore

import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("DragonCore")
object DragonCustomPacket : ObjectiveCountableI<CustomPacketEvent>() {

    override val name = "dragoncore custom packet"
    override val event = CustomPacketEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("name") { data, it ->
            data.toString() == it.identifier
        }
        addSimpleCondition("data") { data, it ->
            it.data.contains(data.toString())
        }
    }
}