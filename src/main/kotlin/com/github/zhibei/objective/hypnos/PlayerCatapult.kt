package com.github.zhibei.objective.hypnos

import com.github.hypnos.api.event.player.PlayerCatapultEvents
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("hypnos")
object PlayerCatapult : ObjectiveCountableI<PlayerCatapultEvents.Pre>() {

    override val name = "Hypnos Catapult"
    override val event = PlayerCatapultEvents.Pre::class.java

    override val isAsync = true

    init {
        handler {
            it.attacker
        }
    }

}