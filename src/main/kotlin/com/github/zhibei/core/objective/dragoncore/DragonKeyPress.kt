package com.github.zhibei.core.objective.dragoncore

import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import eos.moe.dragoncore.api.event.KeyPressEvent
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("DragonCore")
object DragonKeyPress : ObjectiveCountableI<KeyPressEvent>() {

    override val name = "dragoncore keypress"
    override val event = KeyPressEvent::class.java

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