package com.github.zhibei.objective.hypnos

import com.github.hypnos.api.event.player.PlayerSpurtEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("hypnos")
object PlayerSpurt : ObjectiveCountableI<PlayerSpurtEvent>() {

    override val name = "Hypnos Spurt"
    override val event = PlayerSpurtEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
    }

}