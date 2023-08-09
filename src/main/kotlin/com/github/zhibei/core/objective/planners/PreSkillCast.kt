package com.github.zhibei.core.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerCastSkillEvents
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PreSkillCast : ObjectiveCountableI<PlayerCastSkillEvents.Pre>() {

    override val name = "pre cast skill"
    override val event = PlayerCastSkillEvents.Pre::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("skill") { data, e ->
            data.toString() == e.skill.option.name
        }
        addSimpleCondition("level") { data, event ->
            event.player.plannersProfile.job!!.level >= data.toInt()
        }
        addSimpleCondition("job") { data, event ->
            event.player.plannersProfile.job?.name == data.toString()
        }
    }
}