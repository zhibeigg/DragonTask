package com.github.zhibei.core.objective.dragoncore

import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
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