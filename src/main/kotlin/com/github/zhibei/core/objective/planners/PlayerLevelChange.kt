package com.github.zhibei.core.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerLevelChangeEvent
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import kotlin.math.abs

@Loader
@Plugin("Planners")
object PlayerLevelChange : ObjectiveCountableI<PlayerLevelChangeEvent>() {
    override val event: Class<PlayerLevelChangeEvent> = PlayerLevelChangeEvent::class.java
    override val name: String = "player level change"

    init {
        handler {
            it.player
        }
        addSimpleCondition("to") { data, it ->
            it.to >= data.toInt()
        }
        addSimpleCondition("job") { data, it ->
            it.player.plannersProfile.job?.name == data.toString()
        }
        addSimpleCondition("change") { data, it ->
            val result = abs(it.to - it.from)
            result >= data.toInt()
        }
    }
}