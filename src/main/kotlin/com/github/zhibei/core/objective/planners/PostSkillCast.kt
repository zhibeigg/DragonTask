package com.github.zhibei.core.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerCastSkillEvents
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PostSkillCast : ObjectiveCountableI<PlayerCastSkillEvents.Post>() {

    override val name = "post cast skill"
    override val event = PlayerCastSkillEvents.Post::class.java

    override val isAsync = true

    init {
        handler {
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