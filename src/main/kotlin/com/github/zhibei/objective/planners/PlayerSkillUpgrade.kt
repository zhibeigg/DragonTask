package com.github.zhibei.objective.planners

import com.bh.planners.api.PlannersAPI.plannersProfile
import com.bh.planners.api.event.PlayerSkillUpgradeEvent
import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("Planners")
object PlayerSkillUpgrade : ObjectiveCountableI<PlayerSkillUpgradeEvent>() {

    override val name = "planners skill upgrade"
    override val event = PlayerSkillUpgradeEvent::class.java

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
        addSimpleCondition("job") { data, it ->
            it.player.plannersProfile.job?.name == data.toString()
        }
    }
}