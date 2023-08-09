package com.github.zhibei.core.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerSkillBindEvent
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PlayerSkillBind : ObjectiveCountableI<PlayerSkillBindEvent>() {

    override val name = "planners skill bind"
    override val event = PlayerSkillBindEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("skill") { data, it ->
            data.toString() == it.skill.name
        }
        addSimpleCondition("level") { data, it ->
            data.toInt() <= it.skill.level
        }
        addSimpleCondition("slot") { data, it ->
            data.toString() == it.skill.keySlot?.key
        }
        addSimpleCondition("job") { data, it ->
            it.player.plannersProfile.job?.name == data.toString()
        }
    }
}