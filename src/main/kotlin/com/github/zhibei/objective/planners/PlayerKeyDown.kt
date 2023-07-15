package com.github.zhibei.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerKeydownEvent
import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PlayerKeyDown : ObjectiveCountableI<PlayerKeydownEvent>() {

    override val event: Class<PlayerKeydownEvent> = PlayerKeydownEvent::class.java
    override val name: String = "key down"

    init {
        handler {
            it.player
        }
        addSimpleCondition("key") { data, it ->
            it.keySlot.key == data.toString()
        }
        addSimpleCondition("job") { data, it ->
            it.player.plannersProfile.job?.name == data.toString()
        }
        addSimpleCondition("level") { data, it ->
            it.player.plannersProfile.level >= data.toInt()
        }
    }
}