package com.github.zhibei.objective.hypnos

import com.github.hypnos.api.event.player.PlayerCatapultEvents
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("hypnos")
object PlayerCatapulted : ObjectiveCountableI<PlayerCatapultEvents.Post>() {

    override val name = "Hypnos Catapulted"
    override val event = PlayerCatapultEvents.Post::class.java

    override val isAsync = true

    init {
        handler {
            it.damager
        }
        addSimpleCondition("attacker") { data, it ->
            data.toString() == it.attacker.name
        }
    }

}