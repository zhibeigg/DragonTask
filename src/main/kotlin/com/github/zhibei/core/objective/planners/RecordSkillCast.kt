package com.github.zhibei.core.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerCastSkillEvents
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object RecordSkillCast : ObjectiveCountableI<PlayerCastSkillEvents.Record>() {

    override val name = "record cast skill"
    override val event = PlayerCastSkillEvents.Record::class.java

    override val isAsync = true

    init {
        FailureSkillCast.handler {
            it.player
        }
        addSimpleCondition("skill") { data, e ->
            data.toString() == e.skill.option.name
        }
        addSimpleCondition("level") { data, e ->
            e.player.plannersProfile.job!!.level >= data.toInt()
        }
        addSimpleCondition("job") { data, event ->
            event.player.plannersProfile.job?.name == data.toString()
        }
    }
}