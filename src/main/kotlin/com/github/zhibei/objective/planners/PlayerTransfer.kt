package com.github.zhibei.objective.planners

import com.bh.planners.api.event.PlayerTransferEvent
import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PlayerTransfer : ObjectiveCountableI<PlayerTransferEvent>() {

    override val name = "planners transfer"
    override val event = PlayerTransferEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("name") { data, it ->
            data.toString() == it.target.config.name
        }
    }
}