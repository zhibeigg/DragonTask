package com.github.zhibei.objective.dragoncore

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("DragonCore")
object CustomPacket : ObjectiveCountableI<CustomPacketEvent>() {

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