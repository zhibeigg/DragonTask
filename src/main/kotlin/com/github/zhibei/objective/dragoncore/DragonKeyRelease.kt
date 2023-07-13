package com.github.zhibei.objective.dragoncore

import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import eos.moe.dragoncore.api.event.KeyReleaseEvent
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("DragonCore")
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