package com.github.zhibei.objective.hypnos

import com.github.hypnos.api.event.player.PlayerRollEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("hypnos")
object PlayerRoll : ObjectiveCountableI<PlayerRollEvent>() {

    override val name = "Hypnos Roll"
    override val event = PlayerRollEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
    }

}