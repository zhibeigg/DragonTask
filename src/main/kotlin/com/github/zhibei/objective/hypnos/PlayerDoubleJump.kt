package com.github.zhibei.objective.hypnos

import com.github.hypnos.api.event.player.PlayerDoubleJumpEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("hypnos")
object PlayerDoubleJump : ObjectiveCountableI<PlayerDoubleJumpEvent>() {

    override val name = "Hypnos DoubleJump"
    override val event = PlayerDoubleJumpEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
    }

}