package com.github.zhibei.objective.dragoncore

import eos.moe.dragoncore.api.event.KeyReleaseEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("DragonCore")
object DragonKeyRelease : ObjectiveCountableI<KeyReleaseEvent>() {

    override val name = "dragoncore keyrelease"
    override val event = KeyReleaseEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("position") { data, it ->
            data.toPosition().inside(it.player.location)
        }
        addSimpleCondition("key") { data, it ->
            data.toString() == it.key
        }
    }
}